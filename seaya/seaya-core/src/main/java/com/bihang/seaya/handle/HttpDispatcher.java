package com.bihang.seaya.handle;

import com.alibaba.fastjson.JSON;
import com.bihang.seaya.action.param.Param;
import com.bihang.seaya.action.param.impl.ParamMap;
import com.bihang.seaya.action.param.req.SeayaRequest;
import com.bihang.seaya.action.param.req.impl.SeayaHttpRequest;
import com.bihang.seaya.action.param.res.SeayaResponse;
import com.bihang.seaya.action.param.res.impl.SeayaHttpResponse;
import com.bihang.seaya.config.AppConfig;
import com.bihang.seaya.constant.SeayaConstant;
import com.bihang.seaya.context.SeayaContext;
import com.bihang.seaya.intercept.InterceptProcess;
import com.bihang.seaya.log.SeayaLog;
import com.bihang.seaya.route.RouteProcess;
import com.bihang.seaya.route.RouterScanner;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.util.CharsetUtil;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@ChannelHandler.Sharable
public class HttpDispatcher extends SimpleChannelInboundHandler<DefaultHttpRequest> {

    private final AppConfig appConfig = AppConfig.getInstance();
    private final InterceptProcess interceptProcess = InterceptProcess.getInstance();
    private final RouterScanner routerScanner = RouterScanner.getInstance();
    private final RouteProcess routeProcess = RouteProcess.getInstance() ;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DefaultHttpRequest httpRequest) throws Exception {
        SeayaRequest seayaRequest = SeayaHttpRequest.init(httpRequest);
        SeayaResponse seayaResponse = SeayaHttpResponse.init();

        // set current thread request and response
        SeayaContext.setContext(new SeayaContext(seayaRequest, seayaResponse));

        try {
            // request uri
            String uri = seayaRequest.getUrl();
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(URLDecoder.decode(httpRequest.uri(), "utf-8"));

            // check Root Path
            appConfig.checkRootPath(uri, queryStringDecoder);
            //build paramMap
            Param paramMap = buildParamMap(queryStringDecoder);

            //load interceptors
            interceptProcess.loadInterceptors();

            //interceptor before
            boolean access = interceptProcess.processBefore(paramMap);
            if (!access) {
                return;
            }
            // execute Method
            Method method = routerScanner.routeMethod(queryStringDecoder);
            routeProcess.invoke(method,queryStringDecoder) ;


            // interceptor after
            interceptProcess.processAfter(paramMap);

        } catch (Exception e) {
            exceptionCaught(ctx, e);
        } finally {
            // Response
            responseContent(ctx);

            // remove seaya thread context
            SeayaContext.removeContext();
        }


    }

    /**
     * Response
     *
     * @param ctx
     */
    private void responseContent(ChannelHandlerContext ctx) {

        SeayaResponse seayaResponse = SeayaContext.getResponse();
        String context = seayaResponse.getHttpContent() ;

        ByteBuf buf = Unpooled.wrappedBuffer(context.getBytes(StandardCharsets.UTF_8));
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        buildHeader(response);
        ctx.writeAndFlush(response);
    }

    /**
     * build paramMap
     *
     * @param queryStringDecoder
     * @return
     */
    private Param buildParamMap(QueryStringDecoder queryStringDecoder) {
        Map<String, List<String>> parameters = queryStringDecoder.parameters();
        Param paramMap = new ParamMap();
        for (Map.Entry<String, List<String>> stringListEntry : parameters.entrySet()) {
            String key = stringListEntry.getKey();
            List<String> value = stringListEntry.getValue();
            paramMap.put(key, value.get(0));
        }
        return paramMap;
    }




    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        /*if (SeayaException.isResetByPeer(cause.getMessage())){
            return;
        }*/

        SeayaLog.error("请求异常"+cause.toString());

        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, Unpooled.copiedBuffer(JSON.toJSONString(cause.toString()), CharsetUtil.UTF_8));
        buildHeader(response);
        ctx.writeAndFlush(response);
    }

    /**
     * build Header
     *
     * @param response
     */
    private void buildHeader(DefaultFullHttpResponse response) {
        SeayaResponse seayaResponse = SeayaContext.getResponse();

        HttpHeaders headers = response.headers();
        headers.setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        headers.set(HttpHeaderNames.CONTENT_TYPE, seayaResponse.getContentType());
        /**解决ajax跨域*/
        headers.set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN,"http://127.0.0.1");
        headers.set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS,"POST,GET");
        /**解决ajax携带参数*/
        headers.set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS,"true");

        List<io.netty.handler.codec.http.cookie.Cookie> cookies = seayaResponse.cookies();
        for (Cookie cookie : cookies) {
            headers.add(SeayaConstant.ContentType.SET_COOKIE, io.netty.handler.codec.http.cookie.ServerCookieEncoder.LAX.encode(cookie));
        }

    }

}
