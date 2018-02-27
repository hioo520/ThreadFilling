package com.hihuzi.test;

import com.hihuzi.ThreadUtil.annotation.ThreadUtilMethod;
import com.hihuzi.ThreadUtil.annotation.ThreadUtilProperty;
import com.hihuzi.ThreadUtil.annotation.ThreadUtilService;
import com.hihuzi.ThreadUtil.util.ThreadDate;

import java.util.Date;
import java.util.List;

/**
 * @Author: hihuzi 2018/2/27 15:00
 * @Function:  加上这个注解即可
 * @Modifily:
 */
@ThreadUtilService(value = "我是注解的值")
public class demo {
    @ThreadUtilProperty(value = "99")
    private static String name = "初始名字";
    ThreadDate threadDate= new ThreadDate();
    @ThreadUtilMethod
    public void say(){
        List<Object[]> objectsss = threadDate.getMulThreadDate();//提取当前线程数据
        for (Object[] objects : objectsss) {
        System.out.println(Thread.currentThread().getName()+" 执行了  "+objects[0].toString()+" 数据");
        }
        threadDate.setResultData(objectsss);//返回结果集合设置结果
    }
}
