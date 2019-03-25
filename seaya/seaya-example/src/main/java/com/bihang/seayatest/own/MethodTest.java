package com.bihang.seayatest.own;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Created By bihang
 * 2019/1/4 16:30
 */
public class MethodTest {
    public static Integer userId;

    public void say(String s,int a){
        System.out.println(userId);
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        Class<?> aClass = Class.forName("com.bihang.seayatest.own.MethodTest");

        Field userId = aClass.getDeclaredField("userId");
        userId.set(aClass.newInstance(),24);
        System.out.println(MethodTest.userId);


//        Method[] methods = aClass.getMethods();
//        for (Method m :
//                methods) {
//            if ("say".equals(m.getName())){
//                for (Parameter p :
//                        m.getParameters()) {
//                     System.out.println(p.getType());
//                    System.out.println(p.getName());
//                }
//
//                for (Class<?> a :
//                        m.getParameterTypes()) {
//                    System.out.println(a.getName());
//
//                }
//            }
//        }
    }
}
