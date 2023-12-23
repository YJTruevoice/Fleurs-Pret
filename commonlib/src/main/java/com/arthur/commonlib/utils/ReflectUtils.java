package com.arthur.commonlib.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

@Keep
public class ReflectUtils {

    public static <T> T getT(Object o, int i) {
        try {
            ParameterizedType parameter = (ParameterizedType) (o.getClass().getGenericSuperclass());
            return ((Class<T>) parameter.getActualTypeArguments()[i]).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从class声明获取泛型的Class
     * @param o
     * @param i 第几个泛型
     * @param <T>
     * @return
     */
    public static <T> Class<T> getTClassFromObject(Object o, int i) {
        try {
            ParameterizedType parameter = (ParameterizedType) (o.getClass().getGenericSuperclass());
            return (Class<T>) parameter.getActualTypeArguments()[i];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从目标对象o递归向上寻找类型为topTarget的泛型声明，并返回Class，以递归找到的第一个为准
     *
     * @param o         目标对象
     * @param topTarget 目标泛型的上界
     * @param <T>
     * @return
     */
    @Keep
    @Nullable
    public static <T> Class<T> getTargetTFromObj(Object o, Class<?> topTarget) {
        return getTargetTFromObj(o, null, topTarget);
    }

    /**
     * 从目标对象o递归向上寻找类型为topTarget的泛型声明，并返回Class，以递归找到的第一个为准
     *
     * @param o         目标对象
     * @param topObj    目标对象的递归查找上界
     * @param topTarget 目标泛型的上界
     * @param <T>
     * @return
     */
    @Keep
    @Nullable
    public static <T> Class<T> getTargetTFromObj(Object o, Class<?> topObj, Class<?> topTarget) {
        if (o == null || topTarget == null || (topObj != null && !topObj.isAssignableFrom(o.getClass()))) {
            return null;
        }

        try {
            Class<?> temp = o.getClass();
            while (temp != null) {
                if ((temp.getGenericSuperclass() instanceof ParameterizedType)) {
                    // 遍历当前类的泛型声明
                    for (Type type : ((ParameterizedType) (temp.getGenericSuperclass())).getActualTypeArguments()) {
                        // 找到目标泛型
                        if (type instanceof Class && topTarget.isAssignableFrom((Class<?>) type)) {
                            return (Class<T>) type;
                        }
                    }
                }
                // 向当前类父类递归
                temp = temp.getSuperclass();
                // 如果当前类的父类已经超出递归上界，跳出
                if (topObj != null && !topObj.isAssignableFrom(temp)) {
                    temp = null;
                }
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        return null;
    }
}
