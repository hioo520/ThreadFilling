package com.hihuzi.ThreadUtil.util;

import java.util.HashSet;
import java.util.List;

/**
 * @Author: hihuzi 2018/2/27 15:03
 * @Function:
 * @Modifily:
 */
public interface ProxyAPI {
    /**
     * @Author:hihuzi 2018/2/11 16:37  @优化线程数 并且启用线程
     * @Describe: 初始化数据 ①根据数据 大 小 空 优化线程数 ②不能更改先后顺序
     * @Modifily:
     */
    void init(String targetClass, List<Object[]> targetDate, int threadNumber);

    /**
     * @Author:hihuzi 2018/2/23 10:54
     * @Describe: 带阀值控制
     * @Modifily:
     */
    void init(String targetClass, List<Object[]> totalDates, int threadNumber, int limitNumbers);

    /**
     * @Author:hihuzi 2018/2/11 15:14  @线程提取数据
     * @Describe: 每个线程每次只取一条数据 并且清除
     * @Modifily:
     */
    List<Object[]> getMulThreadDate();//提取数据

    /**
     * @Author:hihuzi 2018/2/11 15:16  @线程 保存数据
     * @Describe:
     * @Modifily:
     */
    void setResultData(List<Object[]> accumulateDatas);

    /**
     * @Author:hihuzi 2018/1/28 16:31  @线程数必须小于数据 否则不分数据 线程数至1
     * @Describe: 先整数平分 最后不均分  @影响 线程数目  最小执行数目
     * @Modifily:
     */
    HashSet<List<Object[]>> splitDate(List<Object[]> objects, int divide);

    /**
     * @Author:hihuzi 2018/2/1 20:42  @拼接数据
     * @Describe: 拼接多线程数据输出
     * @Modifily:
     */
    List<Object[]> getMonitorDate(HashSet<List<Object[]>> obj);

    /**
     * @Author:hihuzi 2018/2/11 16:30  @监听器
     * @Describe: 监听器 监听数据返回
     * @Modifily:
     */
    List<Object[]> getResultData();

    /**
     * @Author:hihuzi 2018/2/11 16:29  @清理数据 最后清理静态数据  ???
     * @Describe: 线程取数据 并且清理数据
     * @Modifily:
     */
    void DeleteDate(HashSet<List<Object[]>> obj);

    /**
     * @Author:hihuzi 2018/2/23 15:21
     * @Describe: 本类单例
     * @Modifily:
     */
    Object getThreadDateClass();
}

