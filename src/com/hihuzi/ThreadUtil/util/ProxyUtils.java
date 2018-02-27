package com.hihuzi.ThreadUtil.util;

import com.hihuzi.ThreadUtil.annotation.ThreadUtilMethod;
import com.hihuzi.ThreadUtil.annotation.ThreadUtilProperty;
import com.hihuzi.ThreadUtil.annotation.ThreadUtilService;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author: hihuzi 2018/2/9 9:20
 * @Function: 批处理多线程工具类
 * @Modifily:
 */
public class ProxyUtils {
    private Class<?> clazz;//操作对象的class
    private static Object reflectclass;//反射对象 单例
    private static ProxyUtils proxyUtils;//单例自己

    public ProxyUtils(String classes) {//初始化类
        setClazz(ProxyConstant.getProxyConstant(classes).clazz);
        proxyUtils = this;//同步当前对象
    }

    public ProxyUtils() {
    }

    public synchronized static ProxyUtils getProxyUtils() {
        if (null == proxyUtils) {
            proxyUtils = new ProxyUtils();
        }
        return proxyUtils;
    }

    /**
     * @Author:hihuzi 2018/2/11 8:45
     * @Describe: 懒汉式 单例  同步锁  单例代理对象
     * @Modifily:
     */
    public static Object getReflectClass() {
        synchronized (ProxyUtils.class) {
            if (null == reflectclass) {
                try {
                    reflectclass = getProxyUtils().clazz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return reflectclass;
    }

    /**
     * @Author:hihuzi 2018/2/11 10:24  @动态注入属性  必须是Sting的注入
     * @Describe: 动态注入属性 获取字段上的注解值
     * @Modifily:
     */
    public void doInject(Object object) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException {
        Field[] declaredFields = getProxyUtils().clazz.getDeclaredFields();//获取属性名
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(ThreadUtilProperty.class)) {
//                ThreadUtilProperty annotation1 = declaredField.getAnnotation(ThreadUtilProperty.class);
//                if(null!=annotation1)//没有判断是否有值
                ThreadUtilProperty annotation = declaredField.getAnnotation(ThreadUtilProperty.class);//有注解的并且注解上有赋值
                SetVariate(object, declaredField.getName(), annotation.value());
            }
        }

    }

    /**
     * @Author:hihuzi 2018/2/10 20:56 @
     * @Describe: 加载特定方法 MethodName 解决同一个对象
     * @Modifily:
     */

    public Object goMethod(Object object, String MethodName, List<Object[]> parameterTypes) {
//        for (String parameterType : parameterTypes) {//待修改
//        }
        Method m = null;
        try {
            m = getProxyUtils().clazz.getDeclaredMethod(MethodName, null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        m.setAccessible(true);
        try {
            return m.invoke(object, parameterTypes);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Author:hihuzi 2018/2/10 20:38  @
     * @Describe: 运行含有@ThreadUtilMethod注解  待优化:考虑只运行一次 只注解一次
     * @Modifily:
     */
    public Object runMethod() {
        Object invoke = null;
        Method[] methods = getProxyUtils().clazz.getMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(ThreadUtilMethod.class)) {
                Method me = null;
                try {
                    me = getProxyUtils().clazz.getDeclaredMethod(m.getName(), null);
                    me.setAccessible(true);
                    invoke = me.invoke(getReflectClass(), null);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return invoke;
    }

    public Object runMethod(List<Object[]>... list) {
        Object invoke = null;
        Method[] methods = getProxyUtils().clazz.getMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(ThreadUtilMethod.class)) {
                Method me = null;
                try {
                    me = getProxyUtils().clazz.getDeclaredMethod(m.getName(), null);
                    me.setAccessible(true);
                    invoke = me.invoke(getReflectClass(), list);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return invoke;
    }

    /**
     * @Author:hihuzi 2018/2/10 21:27  @展示 反射代理类对象
     * @Describe: 罗列对象的属性
     * @Modifily:
     */
    public void toShow(Object object) throws IllegalAccessException {
        Field[] fields = getProxyUtils().clazz.getDeclaredFields();
        Field.setAccessible(fields, true);
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i].getName();
            Object value = fields[i].get(object);
            System.out.println("Object: " + object.hashCode() + " name:" + name + " -> value:" + value);
        }
    }

    /**
     * @Author:hihuzi 2018/2/10 20:17  @设置 对象属性
     * @Describe: 操作对象 object, 属性 attribute, 设置的值 variate  ||属性设置值
     * @Modifily:
     */
    public Object SetVariate(Object object, String attribute, String variate) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Field f = getProxyUtils().clazz.getDeclaredField(attribute);
        f.setAccessible(true);
        f.set(object, variate);
        return object;
    }

    /**
     * @Author:hihuzi 2018/2/9 15:07  @判断 类注解ThreadUtilService 输入: XX.class
     * @Describe: 判断是否有给定的注解  Class
     * @Modifily:
     */
    public static Boolean isAnonotation(Class classes) {
//        setClazz(classes);
        return classes.isAnnotationPresent(ThreadUtilService.class);
    }

    /**
     * @Author:hihuzi 2018/2/9 15:09  @判断 类注解ThreadUtilService 输入: Object
     * @Describe: 判断是否有给定的注解  Object
     * @Modifily:
     */
    public static Boolean isAnonotation(Object object) {
//        setClazz(object.getClass());
        return object.getClass().isAnnotationPresent(ThreadUtilService.class);
    }

    /**
     * @Author:hihuzi 2018/2/9 15:09  @判断 类注解ThreadUtilService 输入: 包名
     * @Describe: 判断是否有给定的注解  String  全名
     * @Modifily:
     */
    public static Boolean isAnonotation(String classes) throws ClassNotFoundException {
//        setClazz(Class.forName(classes));
        return Class.forName(classes).isAnnotationPresent(ThreadUtilService.class);
    }

    /**
     * @Author:hihuzi 2018/2/9 16:25  @判断 类注解ThreadUtilService  通过继承来获取
     * @Describe: 判断是否有给定的注解  String  全名  通过继承本类 获取父类
     * @Modifily:
     */
    public Boolean isAnonotationExtends(Object object) {
        Class<?> c = (Class<?>) (this.getClass().getGenericSuperclass());
        setClazz(c);
        return c.isAnnotationPresent(ThreadUtilService.class);
    }

    public void setClazz(Class<?> claz) {
        clazz = claz;
    }

    public ProxyUtils setClazz(String claz) {
        try {
            clazz = Class.forName(claz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ProxyUtils setClazz(Object obj) {
        clazz = obj.getClass();
        return this;
    }

    public Class<?> getClazz() {
        return this.clazz;
    }

}

