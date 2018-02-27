package com.hihuzi.ThreadUtil.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: hihuzi 2018/2/11 14:16
 * @Function:
 * @Modifily:
 */
public class ThreadDate implements Runnable, ProxyAPI {
    private int tip;//当前线程编号  测试
    private static int state = 0;//标记线程
    private static int threadNumbers;//自定义线程数
    private static int limitNumber;//根据待查数量优化线程数目
    private static List<Object[]> targetDates = new ArrayList<Object[]>();//所有数据
    private static HashSet<List<Object[]>> MulThreadDate = new HashSet<List<Object[]>>();//每个线程的任务
    private static HashSet<List<Object[]>> resultData = new HashSet<List<Object[]>>();//累存线程数据 锁控制
    private static ProxyUtils proxyUtil;//多线程反射初始化
    private static ProxyUtils targets;//目标类反射初始化
    private static ThreadDate threadDate = null;//单例

    public ThreadDate() {
        tip = state++;//返回当前线程编号
    }

    /**
     * @Author:hihuzi 2018/2/11 16:37  @优化线程数 并且启用线程
     * @Describe: 初始化数据 ①根据数据 大 小 空 优化线程数 ②不能更改先后顺序
     * @Modifily:
     */
    public void init(String targetClass, List<Object[]> targetDate, int threadNumber) {//初始化数据
        targetDates = targetDate;
        threadNumbers = threadNumber;
        MulThreadDate = splitDate(targetDates, threadNumber);//数据为零或大于线程数 重置线程数为1
        targets = new ProxyUtils(targetClass);//初始目标类
//        proxyUtil = new ProxyUtils().setClazz("com.hihuzi.ThreadUtil.util.ThreadDate");//当前类对象  这个方法可以内容得写死 不会多创建实例
        proxyUtil = new ProxyUtils().setClazz(new ThreadDate());//当前类对象  使用这个方法可以减少因为移动文件带来的麻烦  存在比上面一个多创建一次实例
        ThreadPool.startThread(threadNumbers, proxyUtil);//启动线程
    }

    /**
     * @Author:hihuzi 2018/2/23 15:21
     * @Describe: 本类单例
     * @Modifily:
     */
    public Object getThreadDateClass() {
        synchronized (ThreadDate.class) {
            if (null == threadDate) {
                return new ThreadDate();
            }
        }
        return threadDate;
    }

    /**
     * @Author:hihuzi 2018/2/23 10:54
     * @Describe: 带阀值控制
     * @Modifily:
     */
    public void init(String targetClass, List<Object[]> totalDates, int threadNumber, int limitNumbers) {//初始化数据 运行线程
        init(targetClass, totalDates, threadNumber);
        limitNumber = limitNumbers;
    }

    /**
     * @Author:hihuzi 2018/2/11 16:29  @清理数据 最后清理静态数据  ???
     * @Describe: 线程取数据 并且清理数据
     * @Modifily:
     */
    public void DeleteDate(HashSet<List<Object[]>> obj) {
        obj.clear();
    }

    /**
     * @Author:hihuzi 2018/2/11 16:30  @监听器
     * @Describe: 监听器 监听数据返回
     * @Modifily:
     */
    public List<Object[]> getResultData() {//只有数据保存完成才进行返回  监听器
        while (true) {
            System.out.print("");
            if (0 == threadNumbers) {
                return getMonitorDate(resultData);//进行拼接数据
            }
        }
    }

    /**
     * @Author:hihuzi 2018/2/1 20:42  @拼接数据
     * @Describe: 拼接多线程数据输出
     * @Modifily:
     */
    public List<Object[]> getMonitorDate(HashSet<List<Object[]>> obj) {//监视返回数据
        List<Object[]> objects = new ArrayList<Object[]>();
        Iterator<List<Object[]>> iterator = obj.iterator();//取出数据 从新拼装
        while (iterator.hasNext()) {
            for (Object[] o : iterator.next()) {
                objects.add(o);
            }
        }
//        obj.clear();//清空数据 提高效率最后清理
        return objects;
    }

    /**
     * @Author:hihuzi 2018/1/28 16:31  @线程数必须小于数据 否则不分数据 线程数至1
     * @Describe: 先整数平分 最后不均分  @影响 线程数目  最小执行数目
     * @Modifily:
     */
    public HashSet<List<Object[]>> splitDate(List<Object[]> objects, int divide) {
        HashSet<List<Object[]>> set = new HashSet<List<Object[]>>();
        if (objects.size() > divide && objects.size() != 0 && objects.size() >= limitNumber) {//防止没有数据或者数据小于线程数
            for (int i = 0; i < threadNumbers; i++) {
                List<Object[]> object = new ArrayList<Object[]>();
                int s = objects.size() / divide;
                for (int ii = 0; ii < s; ii++) {
                    object.add(objects.get(0));
                    objects.remove(0);
                }
                set.add(object);
                divide = divide == 0 ? 1 : --divide;
            }
        } else {
            set.add(objects);//空的时候返回一个数组
            threadNumbers = 1;//线程数设置为一条
        }
        return set;
    }

    /**
     * @Author:hihuzi 2018/2/11 15:16  @线程 保存数据
     * @Describe:
     * @Modifily:
     */
    public synchronized void setResultData(List<Object[]> accumulateDatas) {//写入数据
        resultData.add(accumulateDatas);
    }

    /**
     * @Author:hihuzi 2018/2/11 15:14  @线程提取数据
     * @Describe: 每个线程每次只取一条数据 并且清除
     * @Modifily:
     */
    public List<Object[]> getMulThreadDate() {//提取数据
        List<Object[]> li = null;
        synchronized (ThreadDate.class) {
            Iterator<List<Object[]>> iterator = MulThreadDate.iterator();
            li = iterator.next();
            MulThreadDate.remove(li);
        }
        return li;
    }

    @Override
    public void run() {
//        List<Object[]> objectsss = getMulThreadDate();//提取当前线程数据
//        List<Object[]> objects = New.arrayList();
//        if (0 != objectsss.size()) {
        //todo
//        System.out.println(Thread.currentThread().getName() + " ---> ");
        proxyUtil.runMethod();//调用被注解的方法注解的方法
//        System.out.println(Thread.currentThread().getName() + " ---< ");
//            objects = objectsss;//注入
//        } else {
        //todo
//            objects = objectsss;//注入
//        }
//        setResultData(objects);//存储数据
        threadNumbers--;
//        System.out.println("state :" + state++ + "--> " + threadNumbers + " " + Thread.currentThread().getName() + " size: " + objectsss.size());

    }
}
