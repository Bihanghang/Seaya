package com.bihang.seaya;

import com.bihang.seayatest.StartSeayaModifyText;
import org.junit.Test;

/**
 * Created By bihang
 * 2018/12/26 19:16
 */
public class ServerTest {

    @Test
    public void serverTest() throws Exception {
        SeayaServer.start(StartSeayaModifyText.class,8009);
    }
    @Test
    public void singleTest(){

    }
}
