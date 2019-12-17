package com.rd.hcb.spi.impl;

import com.rd.hcb.spi.HelloService;

/**
 * @author zlx
 * @date 2019-12-17 14:31
 */
public class HelloA implements HelloService {
    @Override
    public void sayHello() {
        System.out.println("A say hello");
    }
}
