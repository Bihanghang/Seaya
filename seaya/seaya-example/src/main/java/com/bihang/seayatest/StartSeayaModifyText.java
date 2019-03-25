package com.bihang.seayatest;


import com.bihang.seaya.SeayaServer;

import static com.bihang.seaya.Seaya.text;

/**
 * Created By bihang
 * 2018/12/21 17:31
 */
public class StartSeayaModifyText {
    public static void main(String[] args) throws Exception {
        new SeayaServer().start(StartSeayaModifyText.class,()->text("Who are you?"));
    }
}
