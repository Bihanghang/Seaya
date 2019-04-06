package com.bihang.seaya.example.action;

import com.bihang.seaya.Seaya;
import com.bihang.seaya.action.param.req.WorkReq;
import com.bihang.seaya.action.param.res.WorkRes;
import com.bihang.seaya.annotation.SeayaAction;
import com.bihang.seaya.annotation.SeayaRoute;
import com.bihang.seaya.configuration.ConfigurationHolder;
import com.bihang.seaya.context.SeayaContext;
import com.bihang.seaya.example.configuration.KafkaConfiguration;
import com.bihang.seaya.example.req.DemoReq;
import com.bihang.seaya.log.SeayaLog;

@SeayaAction("routeAction")
public class RouteAction {

    @SeayaRoute("getUser")
    public void getUser(DemoReq req){

        SeayaLog.log(req.toString());
        WorkRes<DemoReq> reqWorkRes = new WorkRes<>() ;
        reqWorkRes.setMessage("hello =" + req.getName());
        reqWorkRes.setCode("2");
        SeayaContext.getContext().json(reqWorkRes) ;
    }

    @SeayaRoute("getUser2")
    public void getUser2(DemoReq req){

        SeayaLog.log(req.toString());
        WorkRes<DemoReq> reqWorkRes = new WorkRes<>() ;
        reqWorkRes.setMessage("hello =" + req.getName());
        reqWorkRes.setCode("2");
        SeayaContext.getContext().text(reqWorkRes.toString());
    }

    @SeayaRoute("getUser3")
    public void getUser3(DemoReq req){
        SeayaLog.log(req.toString());
        WorkRes<DemoReq> reqWorkRes = new WorkRes<>() ;
        reqWorkRes.setMessage("hello =" + req.getName());
        reqWorkRes.setCode("2");
        SeayaContext.getContext().html("<html><body>"+reqWorkRes.toString()+"</body></html>"); ;
    }

    @SeayaRoute("testConfiguration")
    public void testConfiguration(){
        KafkaConfiguration kafkaConfiguration = (KafkaConfiguration) ConfigurationHolder.getConfiguration(KafkaConfiguration.class);
        String s = kafkaConfiguration.get("kafka.broker.list");
        System.out.println("kafka.broker.list "+s);
        SeayaContext.getContext().text("kafka.broker.list "+s);
    }


}
