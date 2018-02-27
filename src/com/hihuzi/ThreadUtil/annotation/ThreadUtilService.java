package com.hihuzi.ThreadUtil.annotation;

import java.lang.annotation.*;

/**
 * @Author: hihuzi 2018/2/8 17:03
 * @Function:
 * @Modifily:
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ThreadUtilService {
    String value() default "";
}

