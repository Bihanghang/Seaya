package com.bihang.seaya;

import com.bihang.seayatest.own.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created By bihang
 * 2019/1/16 14:06
 */
public class GenericTest {

    public static void main(String[] args) {
//        Gson gson = new Gson();
//        String json = "{\"userId\":\"2\",\"userName\":\"bihang\"}";
//        User res = gson.fromJson(json,new TypeToken<User>() {}.getType());
//        System.out.println(res.toString());
        ba();

    }
    public static Response<User> ba(){
        User biahgn = back(new User(1, "biahgn"));
        System.out.println(biahgn.toString());
        return null;
    }

    public static <T,U> T back(U param){
        Gson gson = new Gson();
        String response = gson.toJson(param);
        System.out.println(response);
        String json = "{\"userId\":\"2\",\"userName\":\"bihang\"}";
        T res = gson.fromJson(json,new TypeToken<T>() {}.getType());
        return res;
    }
}

class Response<T>{
    public T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
