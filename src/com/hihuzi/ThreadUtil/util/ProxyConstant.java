package com.hihuzi.ThreadUtil.util;

/**
 * @Author:hihuzi 2018/2/24 8:48
 * @Describe:
 * @Modifily:
 */
public class ProxyConstant {
    public Class<?> clazz;//操作对象的class
    private Object reflectclass;//反射对象 单例
    private static ProxyConstant proxyConstant;//单例自己

    private ProxyConstant() {
    }

    private ProxyConstant(String classes) {
        try {
            clazz = Class.forName(classes);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        proxyConstant = this;//同步自己
    }

    /**
     * @Author:hihuzi 2018/2/11 8:45
     * @Describe: 懒汉式 单例  同步锁
     * @Modifily:
     */
    public static ProxyConstant getProxyConstant(String classes) {
        synchronized (ProxyConstant.class) {
            if (null == proxyConstant) {
                proxyConstant = new ProxyConstant(classes);
            }
        }
        return proxyConstant;
    }
}

