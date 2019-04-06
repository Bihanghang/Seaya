package com.bihang.seaya.example;

import com.bihang.seaya.SeayaServer;

public class MainStart {
    public static void main(String[] args) throws Exception {
        SeayaServer.start(MainStart.class,"/seaya-example") ;
    }
}
