package com.bihang.seaya;

/**
 * Created By bihang
 * 2019/1/17 9:42
 */
public class GenerTest extends BaseController {

    public Response2<String> back(){
        String s = "lj";
       return success(s);
    }

    public static void main(String[] args) {
        GenerTest generTest = new GenerTest();
        Response2<String> back = generTest.back();
    }
}
