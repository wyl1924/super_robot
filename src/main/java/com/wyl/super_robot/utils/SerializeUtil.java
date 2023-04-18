package com.wyl.super_robot.utils;

/**
 * @创建人 王延领
 * @创建时间 2021/12/5
 * 描述
 **/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {

    /*
     * 序列化
     * */
    public static byte[] serialize(Object object){
        //序列化流 （输出流） --> 表示向一个目标 写入数据
        ObjectOutputStream objectOutputStream =null;
        //字节数组输出流
        ByteArrayOutputStream byteArrayOutputStream = null;
        try{
            //创建一个缓冲区
            byteArrayOutputStream = new ByteArrayOutputStream();
            //将 对象 序列化成 字节后  输入缓冲区中
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            //序列化 对象
            objectOutputStream.writeObject(object);
            //得 到 序列化字节
            byte[] bytes = byteArrayOutputStream.toByteArray();

            //清空输出流
            objectOutputStream.flush();
            //释放资源
            objectOutputStream.close();

            return bytes;
        }catch (Exception e){
            System.out.println("出现异常:"+e.getMessage());
        }
        return null;
    }

    /*
     * 反序列化
     * */

    public static  <T> T deserialize(byte[] bytes,Class<T> clazz){
        //字节数组
        ByteArrayInputStream byteArrayInputStream = null;
        try{
            //将 得到的序列化字节 丢进 缓冲区
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            //反序列化流 （输入流）--> 表示着从 一个 源头 读取 数据 ， （读取 缓冲区中 的字节）
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            //反序化成 一个对象
            return (T)objectInputStream.readObject();
        }catch (Exception e){
            System.out.println("出现异常:"+e.getMessage());
        }
        return null;
    }


}