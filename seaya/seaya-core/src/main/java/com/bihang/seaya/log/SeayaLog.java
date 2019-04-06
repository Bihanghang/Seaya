package com.bihang.seaya.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created By bihang
 * 2019/3/21 18:08
 */
public class SeayaLog {

    public static Logger seaya_log_console = LoggerFactory.getLogger("seaya_log_console");
    public static Logger seaya_log_file = LoggerFactory.getLogger("seaya_log_file");

    public static void log(String messageContent) {
        seaya_log_console.info(messageContent);
        seaya_log_file.info(messageContent);
    }



    public static void error(String messageContent) {
        seaya_log_console.error(messageContent);
        seaya_log_file.error(messageContent);
    }
}
