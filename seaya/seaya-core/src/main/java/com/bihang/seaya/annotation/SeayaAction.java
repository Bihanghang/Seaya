package com.bihang.seaya.annotation;


import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SeayaAction {

    String value() default "" ;
}
