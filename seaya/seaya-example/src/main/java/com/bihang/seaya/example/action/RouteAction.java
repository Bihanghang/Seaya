package com.bihang.seaya.example.action;

import com.bihang.seaya.action.param.req.Cookie;
import com.bihang.seaya.action.param.res.WorkRes;
import com.bihang.seaya.annotation.SeayaAction;
import com.bihang.seaya.annotation.SeayaRoute;
import com.bihang.seaya.configuration.ConfigurationHolder;
import com.bihang.seaya.context.SeayaContext;
import com.bihang.seaya.example.configuration.KafkaConfiguration;
import com.bihang.seaya.example.req.DemoReq;
import com.bihang.seaya.example.req.UserReq;
import com.bihang.seaya.log.SeayaLog;

@SeayaAction("routeAction")
public class RouteAction {

    @SeayaRoute("getUser")
    public void getUser(DemoReq req){

        SeayaLog.log(req.toString());
        WorkRes<DemoReq> reqWorkRes = new WorkRes<>() ;
        reqWorkRes.setMessage("hello =" + req.getName());
        reqWorkRes.setCode("2");
        if (SeayaContext.getRequest().getCookie("id") != null ){
            System.out.println("cookie"+SeayaContext.getRequest().getCookie("id").toString());
        }
        Cookie cookie = new Cookie();
        cookie.setName("id");
        cookie.setValue("3535465");
        SeayaContext.getResponse().setCookie(cookie);
        SeayaContext.getContext().json(reqWorkRes) ;
    }

    @SeayaRoute("getUser2")
    public void getUser2(DemoReq req){

        SeayaLog.log(req.toString());
        WorkRes<DemoReq> reqWorkRes = new WorkRes<>() ;
        reqWorkRes.setMessage("hello =" + req.getName());
        reqWorkRes.setCode("2");
        SeayaContext.getContext().text(reqWorkRes.toString());
    }

    @SeayaRoute("getUser3")
    public void getUser3(DemoReq req){
        SeayaLog.log(req.toString());
        WorkRes<DemoReq> reqWorkRes = new WorkRes<>() ;
        reqWorkRes.setMessage("hello =" + req.getName());
        reqWorkRes.setCode("2");
        SeayaContext.getContext().html("<html><body>"+reqWorkRes.toString()+"</body></html>"); ;
    }

    @SeayaRoute("testConfiguration")
    public void testConfiguration(){
        KafkaConfiguration kafkaConfiguration = (KafkaConfiguration) ConfigurationHolder.getConfiguration(KafkaConfiguration.class);
        String s = kafkaConfiguration.get("kafka.broker.list");
        System.out.println("kafka.broker.list "+s);
        SeayaContext.getContext().text("kafka.broker.list "+s);
    }

    @SeayaRoute("login")
    public void login(UserReq user){
        SeayaLog.log(user.toString());
        WorkRes<UserReq> reqWorkRes = new WorkRes<>() ;
        if (user.getUsername().equals("tom") && user.getPassword().equals("123")){
            if (user.getRemember().equals("1")){
                Cookie cookie = new Cookie();
                cookie.setName("tom");
                cookie.setValue("123");
                SeayaContext.getResponse().setCookie(cookie);
            } else {
                Cookie cookie = new Cookie();
                cookie.setName("login");
                cookie.setValue("yes");
                SeayaContext.getResponse().setCookie(cookie);
            }
            reqWorkRes.setMessage("验证通过");
            reqWorkRes.setCode("0");
            SeayaContext.getContext().json(reqWorkRes) ;
        } else {
            reqWorkRes.setMessage("验证失败");
            reqWorkRes.setCode("1");
            SeayaContext.getContext().json(reqWorkRes) ;
        }
    }

    @SeayaRoute("islogin")
    public void islogin(){
        SeayaLog.log("进入验证登陆url");
        WorkRes workRes = new WorkRes();
        if (SeayaContext.getRequest().getCookie("tom") != null ){
            /**此处注意，倘若cookie不存在，会报空指针异常*/
            if (SeayaContext.getRequest().getCookie("tom").getValue().equals("123")){
                workRes.setCode("3");
                workRes.setMessage("验证登陆通过");
            }
        } else if(SeayaContext.getRequest().getCookie("login") != null) {
            /**此处注意，倘若cookie不存在，会报空指针异常*/
            if (SeayaContext.getRequest().getCookie("login").getValue().equals("yes")){
                workRes.setCode("4");
                workRes.setMessage("验证登陆通过");
            }
        }
        SeayaContext.getContext().json(workRes) ;
    }

}
