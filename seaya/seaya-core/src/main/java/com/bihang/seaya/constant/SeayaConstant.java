package com.bihang.seaya.constant;

public class SeayaConstant {
    public final static String SEAYA_PORT = "seaya.port";
    public final static String ROOT_PATH = "seaya.root.path";

    public static final class ContentType {
        public final static String JSON = "application/json; charset=UTF-8";
        public final static String TEXT = "text/plain; charset=UTF-8";
        public final static String HTML = "text/html; charset=UTF-8";
        public final static String SET_COOKIE = "Set-Cookie";
        public final static String COOKIE = "Cookie";
    }

    public static final class SystemProperties {
        static final String NEW_LINE = "\r\n";
        public static final String LOGO = NEW_LINE+
                String.format("%1$54s","__  __   _       _")+NEW_LINE+
                String.format("%1$55s","}_  }_  /_\\ |_| /_\\")+NEW_LINE+
                String.format("%1$55s","__| }_  { }  _| { }")+NEW_LINE+
                String.format("%1$50s","::"+"v.1.0"+"::")+NEW_LINE;
        public final static String APPLICATION_PROPERTIES = "application.properties";
        public final static String APPLICATION_THREAD_MAIN_NAME = "☝( ◠‿◠ )☝";
        public final static String APPLICATION_THREAD_WORK_NAME = "(♛‿♛)";
        public final static String APPLICATION_THREAD_SHUTDOWN_NAME = "(〒︿〒)";
    }

}
