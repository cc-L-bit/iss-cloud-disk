package com.iss.cloud.disk.configure;

/*
 *功能描述
 * @author tangfl
 * @description
 * @date
 */
public class Test {
    public static void main(String[] args){
        // /tfl//abc/    /tfl/tangfengliang//456/
        String a = "/tfl//abc/";
        System.out.println("a = " + a);
        if (a.contains("//")){
            a = a.replace("//", "/");
            System.out.println("-- a = " + a);
        }

        String b = "/tfl/tangfengliang//456//gui保存模型截图01.png";
        System.out.println("b = " + b);
        if (b.contains("//")){
            b = b.replace("//", "/");
            System.out.println("-- b = " + b);
        }


    }
}
