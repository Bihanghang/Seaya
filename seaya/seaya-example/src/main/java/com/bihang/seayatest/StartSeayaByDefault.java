package com.bihang.seayatest;

import com.bihang.seaya.SeayaServer;

/**
 * Created By bihang
 * 2018/12/29 19:53
 */
public class StartSeayaByDefault {
    public static void main(String[] args) throws Exception {
        SeayaServer.start(StartSeayaByDefault.class);
    }
}
