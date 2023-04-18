package com.wyl.super_robot.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @创建人 王延领
 * @创建时间 2021/11/30
 * 描述 对象工具类
 **/
public class PageUtil {

    /**
     * 如果pageIndex或pageSize为null，则设置默认值
     *
     * @param obj
     * @return
     */
    public static Object setPageDefault(Object obj) {
        List<Field> fieldList = getAllFields(obj);
        for (Field field : fieldList) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            // 得到此属性的名
            String name = field.getName();
            if (!"pageIndex".equals(name) && !"pageSize".equals(name)) {
                continue;
            }
            // 设置此属性是可以访问的
            field.setAccessible(true);
            try {
                // 得到此属性的值
                Object val = field.get(obj);
                if (val == null && "pageIndex".equals(name)) {
                    field.set(obj, 1);
                } else if (val == null && "pageSize".equals(name)) {
                    field.set(obj, 10);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    /**
     * 获取当前类及父类属性
     *
     * @param object
     * @return
     */
    public static List<Field> getAllFields(Object object) {
        Class clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        return fieldList;
    }
}
