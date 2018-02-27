package com.hihuzi.ThreadUtil.annotation;

import java.lang.annotation.*;

/**
 * @Author: hihuzi 2018/2/10 15:33
 * @Function:
 * @Modifily:
 */

@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ThreadUtilProperty {
    String value() default "";
}


