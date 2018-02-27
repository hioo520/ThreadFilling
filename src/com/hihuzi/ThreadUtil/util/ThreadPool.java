package com.hihuzi.ThreadUtil.util;

import java.util.HashSet;
import java.util.Iterator;

/**
 * @Author: hihuzi 2018/2/11 14:16
 * @Function:
 * @Modifily:
 */
public class ThreadPool {
    /**
     * @Author:hihuzi 2018/2/10 22:05
     * @Describe: 线程运行
     * @Modifily:
     */

    public static void startThread(int threadNumbers, ProxyUtils obj) {//启动多线程
        Iterator<Thread> iterator = getThread(threadNumbers, obj).iterator();
        while (iterator.hasNext()) {
            iterator.next().start();//运行线程
//            thread.remove(iterator.next());
        }
    }

    /**
     * @Author:hihuzi 2018/2/10 22:05
     * @Describe: 线程池 创建 数量是j个
     * @Modifily:
     */

    public static HashSet<Thread> getThread(int j, ProxyUtils obj) {
        HashSet<Thread> set = new HashSet<Thread>();
        for (int i = 0; i < j; i++) {
            try {
                set.add(new Thread((Runnable) obj.getClazz().newInstance()));//创建对象
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return set;
    }
}
