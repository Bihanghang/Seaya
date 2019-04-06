package com.bihang.seaya.example.req;

import com.bihang.seaya.action.param.req.WorkReq;


public class DemoReq extends WorkReq {

    private Integer id ;
    private String name ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DemoReq{" +
                "id=" + id +
                ", name='" + name + '\'' +
                "} " + super.toString();
    }
}
