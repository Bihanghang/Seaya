package com.bihang.seaya.config;

import com.bihang.seaya.enums.StatusEnum;
import com.bihang.seaya.exception.SeayaException;
import com.bihang.seaya.util.PathUtil;
import io.netty.handler.codec.http.QueryStringDecoder;

public class AppConfig {

    private AppConfig() {
    }


    /**
     * 简单单例(线程非安全)
     */
    private static AppConfig config;

    public static AppConfig getInstance() {
        if (config == null) {
            config = new AppConfig();
        }
        return config;
    }

    private String rootPackageName;

    private String rootPath;

    private Integer port = 7318;

    public String getRootPackageName() {
        return rootPackageName;
    }

    public void setRootPackageName(Class<?> clazz) {
        if (clazz.getPackage() == null) {
            throw new SeayaException(StatusEnum.NULL_PACKAGE, "[" + clazz.getName() + ".java]:" + StatusEnum.NULL_PACKAGE.getMessage());
        }
        this.rootPackageName = clazz.getPackage().getName();
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }


    /**
     * check Root Path
     *
     * @param uri
     * @param queryStringDecoder
     * @return
     */
    public void checkRootPath(String uri, QueryStringDecoder queryStringDecoder) {
        if (!PathUtil.getRootPath(queryStringDecoder.path()).equals(this.getRootPath())) {
            throw new SeayaException(StatusEnum.REQUEST_ERROR, uri);
        }
    }
}
