package com.bihang.seaya.annotation;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SeayaRoute {

    String value() default "" ;
}
