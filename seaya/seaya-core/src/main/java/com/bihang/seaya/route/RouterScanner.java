package com.bihang.seaya.route;

import com.bihang.seaya.Seaya;
import com.bihang.seaya.annotation.SeayaAction;
import com.bihang.seaya.annotation.SeayaRoute;
import com.bihang.seaya.config.AppConfig;
import com.bihang.seaya.context.SeayaContext;
import com.bihang.seaya.enums.StatusEnum;
import com.bihang.seaya.exception.SeayaException;
import com.bihang.seaya.reflect.ClassScanner;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class RouterScanner {

    private static Map<String, Method> routes = null;

    private volatile static RouterScanner routerScanner;

    private AppConfig appConfig = AppConfig.getInstance();

    /**
     * get single Instance
     *
     * @return
     */
    public static RouterScanner getInstance() {
        if (routerScanner == null) {
            synchronized (RouterScanner.class) {
                if (routerScanner == null) {
                    routerScanner = new RouterScanner();
                }
            }
        }
        return routerScanner;
    }

    private RouterScanner() {
    }

    /**
     * get route method
     *
     * @param queryStringDecoder
     * @return
     * @throws Exception
     */
    public Method routeMethod(QueryStringDecoder queryStringDecoder) throws Exception {
        if (routes == null) {
            routes = new HashMap<>(16);
            loadRouteMethods(appConfig.getRootPackageName());
        }

        //default response
        boolean defaultResponse = defaultResponse(queryStringDecoder.path());

        if (defaultResponse) {
            return null;
        }

        Method method = routes.get(queryStringDecoder.path());

        if (method == null) {
            throw new SeayaException(StatusEnum.NOT_FOUND);
        }

        return method;


    }

    private boolean defaultResponse(String path) {
        if (appConfig.getRootPath().equals(path)) {
            SeayaContext.getContext().html("<center> Hello Seaya <br/><br/>" +
                    "Power by @Seaya </center>");
            return true;
        }
        return false;
    }


    private void loadRouteMethods(String packageName) throws Exception {
        Set<Class<?>> classes = ClassScanner.getClasses(packageName);

        for (Class<?> aClass : classes) {
            Method[] declaredMethods = aClass.getMethods();

            for (Method method : declaredMethods) {
                SeayaRoute annotation = method.getAnnotation(SeayaRoute.class);
                if (annotation == null) {
                    continue;
                }

                SeayaAction seayaAction = aClass.getAnnotation(SeayaAction.class);
                routes.put(appConfig.getRootPath() + "/" + seayaAction.value() + "/" + annotation.value(), method);
            }
        }
    }

}
