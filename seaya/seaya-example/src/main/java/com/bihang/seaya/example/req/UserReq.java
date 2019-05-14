package com.bihang.seaya.example.req;

public class UserReq {

    String username;
    String password;
    String remember;

    public UserReq() {
    }

    public UserReq(String username, String password, String remember) {
        this.username = username;
        this.password = password;
        this.remember = remember;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemember() {
        return remember;
    }

    public void setRemember(String remember) {
        this.remember = remember;
    }

    @Override
    public String toString() {
        return "UserReq{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", remember='" + remember + '\'' +
                '}';
    }
}
