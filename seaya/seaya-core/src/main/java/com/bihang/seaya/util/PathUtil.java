package com.bihang.seaya.util;

public class PathUtil {

    /**
     * 获取到根目录
     * /seaya-example/demoAction
     * @param path
     * @return seaya-example
     */
    public static String getRootPath(String path) {
        return "/" + path.split("/")[1];
    }
}
