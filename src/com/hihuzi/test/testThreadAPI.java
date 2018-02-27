package com.hihuzi.test;

import com.hihuzi.ThreadUtil.util.ProxyAPI;
import com.hihuzi.ThreadUtil.util.ThreadDate;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hihuzi 2018/2/11 14:17
 * @Function: 说明  多线程装填工具
 * @Modifily: 使用
 * _1.在使用方法上添加注解@ThreadUtilMethod
 * * _1.1 注解的方法没有参数传递
 * _2.调用重载这个方法ThreadDate.init(String targetClass, List<Object[]> targetDate, int threadNumber)
 * *_2.1 targetClass:    需要进行处理的方法类的全限定名
 * *_2.2 targetDate:     待处理数据的集合
 * *_2.3 threadNumber:   线程数量
 * _3.ThreadDate.getResultData() 取出数据
 * 说明
 * A.目标数据量不能低于线程数目  否则跑一个线程
 * B. 可以根据目标数据量阈值进行启用或者调用线程数目  init(String targetClass, List<Object[]> totalDates, int threadNumber, int limitNumbers)  limitNumbers :设置阈值 大于时候启用线程 否则启用一个线程
 */

public class testThreadAPI {

    public static void main(String[] args) {
        ProxyAPI api= new ThreadDate();//调用统一接口
        int tip = 3;
        List<Object[]> targetDate = new ArrayList<Object[]>();
        for (int i = 0; i <= tip; i++) {
            Object[] o = new Object[]{"你好师姐!!!"};
            targetDate.add(o);
        }

        api.init("com.hihuzi.test.demo", targetDate, tip);//并且运行多线程
//        List<Object[]> resultData = ThreadDate.getResultData();//获取数据
        System.out.println(" size: " + api.getResultData().size());//提取数据

    }
}
