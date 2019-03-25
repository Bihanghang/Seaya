package com.bihang.seayatest;

import com.bihang.seaya.SeayaServer;

/**
 * Created By bihang
 * 2019/1/3 10:36
 */
public class StartSeayaModifyPort {
    public static void main(String[] args) throws Exception {
        new SeayaServer().start(StartSeayaModifyPort.class,8009);
    }
}
