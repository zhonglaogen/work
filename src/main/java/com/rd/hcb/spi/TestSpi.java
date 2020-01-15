package com.rd.hcb.spi;

import java.util.ServiceLoader;

/**
 * SPI全称为Service Provider Interface，是JDK内置的一种服务提供发现机制。简单来说，
 * 它就是一种动态替换发现机制。例如：有个接口想在运行时才发现具体的实现类，
 * 那么你只需要在程序运行前添加一个实现即可，并把新加的实现描述给JDK即可。
 * 此外，在程序的运行过程中，也可以随时对该描述进行修改，完成具体实现的替换。
 *
 * 那么问题来了，SPI的接口是Java核心库的一部分，是由引导类加载器(Bootstrap Classloader)来加载的。
 * SPI的实现类是由系统类加载器(System ClassLoader)来加载的。
 *
 * 引导类加载器在加载时是无法找到SPI的实现类的，因为双亲委派模型中规定，
 * 引导类加载器BootstrapClassloader无法委派系统类加载器AppClassLoader来加载。这时候，该如何解决此问题？
 *
 * 线程上下文类加载由此诞生，它的出现也破坏了类加载器的双亲委派模型，使得程序可以进行逆向类加载。
 *
 *
 * @author zlx
 * @date 2019-12-17 14:34
 */
public class TestSpi {
    public static void main(String[] args) {
        ServiceLoader<HelloService> hs =ServiceLoader.load(HelloService.class);
        for (HelloService hhs : hs){
            hhs.sayHello();
        }
        String s= "dasd";
        System.out.println(s.indexOf('t'));
    }
}
