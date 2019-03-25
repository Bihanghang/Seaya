package com.bihang.seayatest.own;

import com.bihang.seaya.annotation.SeayaMapping;


/**
 * Created By bihang
 * 2018/12/26 19:33
 */
@SeayaMapping("/say/saya2/say3")
public class SeayaAction {

    @SeayaMapping("/hello/haha")
    public User sayHello(){
        User user = new User();
        user.setUserId(8);
        user.setUserName("影流之主");
        return user;
    }

    @SeayaMapping("/hello")
    public Object sayHello2(User bihang){
        System.out.println(bihang.toString());
        return bihang;
    }
}
