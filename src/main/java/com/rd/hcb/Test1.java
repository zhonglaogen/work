package com.rd.hcb;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Test1
 *
 * @author zhonglx
 * @version 1.0.0 2021/9/14 12:50
 */
public class Test1 {

    public static void main(String[] args) {

        ReentrantLock r  = new ReentrantLock();

        r.lock();
        new Thread(() -> {
            r.lock();
        }).start();
    }
}
