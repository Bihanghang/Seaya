package com.bihang.seaya.response;

import com.alibaba.fastjson.JSON;
import com.bihang.seaya.annotation.SeayaMapping;
import com.bihang.seaya.environment.Environment;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * Created By bihang
 * 2018/12/29 15:15
 */
@Slf4j
public class BuildResponse {
    public static Environment environment = Environment.getInstance();

    public static Set<Class<?>> classes = new HashSet<>();

    public static DefaultFullHttpResponse build(String uri, DefaultHttpRequest request) throws IOException, IllegalAccessException, InstantiationException, InvocationTargetException {
        DefaultFullHttpResponse response = null;
        if ("/".equals(uri)) {
            //构建默认返回对象
            ResponJson<String> responJson = new ResponJson<>();
            responJson.setCode(1);
            responJson.setMessage("成功");
            responJson.setData(environment.getText());

            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.NOT_FOUND, Unpooled.copiedBuffer(JSON.toJSONString(responJson), CharsetUtil.UTF_8));
            setHeader(response);
            return response;
        } else if( !"/favicon.ico ".equals(uri)){
            //执行http地址对应的方法
            response = excuteSeayaMapping(uri);

            setHeader(response);

        }
        return response;
    }


    /**
     * 执行http地址对应的方法，如果该http地址没有对应的方法，默认返回something wrong.
     *
     * @param uri
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IOException
     */
    public static DefaultFullHttpResponse excuteSeayaMapping(String uri) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {

        DefaultFullHttpResponse response = null;

        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        String uriPath = decoder.path();
        log.info("uriPath" + uriPath);

        String rootPackageName = environment.getRootPackageName();
        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(rootPackageName.replace(".", "/"));
        System.out.println(dirs);
        while (dirs.hasMoreElements()) {
            URL url = dirs.nextElement();
            System.out.println(url);
            //获取包的物理路径
            String classPath = url.getFile();
            System.out.println(classPath);

            addPathToClasses(classPath, rootPackageName, classes);
            //用来与uri比对，如果一致就执行方法。
            String fullmapping = "";
            for (Class<?> aClass :
                    classes) {
                for (Annotation classA :
                        aClass.getAnnotations()) {
                    if (classA instanceof SeayaMapping) {
                        fullmapping = ((SeayaMapping) classA).value();
                        for (Method m :
                                aClass.getMethods()) {
                            for (Annotation a :
                                    m.getAnnotations()) {
                                if (a instanceof SeayaMapping) {
                                    fullmapping += ((SeayaMapping) a).value();
                                    if (fullmapping.equals(uriPath)) {
                                        Class<?>[] parameterTypes = m.getParameterTypes();
                                        Object[] instances = new Object[parameterTypes.length];
                                        for (int i = 0; i <parameterTypes.length;i++){
                                            Class parameterClass = parameterTypes[i];
                                            Map<String, List<String>> parameters = decoder.parameters();
                                            Object instance = parameterClass.newInstance();
                                            parameters.forEach((k,v)->{
                                                try {
                                                    Field field = parameterClass.getDeclaredField(k);
                                                    field.setAccessible(true);

                                                    field.set(instance,(field.getType().equals(int.class) || field.getType().equals(Integer.class))? Integer.valueOf(v.get(0)):v.get(0));
                                                } catch (NoSuchFieldException e) {
                                                    e.printStackTrace();
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                }
                                            });
                                            instances[i] = instance;
                                        }
                                        
                                        
                                        
                                        Object o = m.invoke(aClass.newInstance(),instances);
                                        response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                                                HttpResponseStatus.NOT_FOUND, Unpooled.copiedBuffer(JSON.toJSONString(o), CharsetUtil.UTF_8));
                                        setHeader(response);
                                        return response;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.NOT_FOUND, Unpooled.copiedBuffer(JSON.toJSONString("Something is wrong"), CharsetUtil.UTF_8));
    }


    /**
     * 构建返回头
     *
     * @param response
     * @return
     */
    public static DefaultFullHttpResponse setHeader(DefaultFullHttpResponse response) {
        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
        return response;
    }

    /**
     * 获取target包下所有的.class文件
     *
     * @param classPath       classpath的绝对路径地址
     * @param rootPackageName 包名
     * @param classes
     */
    public static void addPathToClasses(String classPath, String rootPackageName, Set<Class<?>> classes) {
        File file = new File(classPath);
        if (!file.exists() && !file.isDirectory())
            return;

        if (file.isDirectory()) {
            File[] list = file.listFiles();
            //如果是文件夹就需要在包名后面添加文件名
            for (File path :
                    list) {
                if (path.isDirectory())
                    addPathToClasses(path.getAbsolutePath(), rootPackageName + "." + path.getName(), classes);
                else
                    addPathToClasses(path.getAbsolutePath(), rootPackageName, classes);
            }
        } else {
            if (file.getName().endsWith(".class")) {
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                try {
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(rootPackageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
