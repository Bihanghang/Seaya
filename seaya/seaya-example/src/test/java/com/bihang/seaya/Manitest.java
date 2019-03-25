package com.bihang.seaya;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created By bihang
 * 2019/1/15 14:09
 */
public class Manitest {
    public static void main(String[] args) throws ParseException {
        List<TestU> testUS = new ArrayList<>();
        testUS.add(new TestU(20180203,"12:12"));
        testUS.add(new TestU(20180103,"12:19"));
        testUS.add(new TestU(20180503,"2:12"));
        testUS.add(new TestU(20180903,"12:19"));
        testUS.add(new TestU(20180903,"12:15"));
        testUS.add(new TestU(20180903,"12:15"));
        Collections.sort(testUS, getComparator());
        System.out.println(testUS.toString());

    }

    public static Comparator<TestU> getComparator(){
        return (o1,o2) ->{
            try {
                if (o1.getDate() < o2.getDate()){
                    return -1;
                } else if (o1.getDate().equals(o2.getDate()) ){
                    if (new SimpleDateFormat("HH:mm").parse(o1.getTime()).before(new SimpleDateFormat("HH:mm").parse(o2.getTime()))){
                        return -1;
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 1;
        };
    }
}
@Data
class TestU {
    Integer date;
    String time;

    public TestU(Integer date, String time) {
        this.date = date;
        this.time = time;
    }
}