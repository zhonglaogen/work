package com.rd.hcb.procon;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zlx
 * @date 2019-11-01 15:55
 */
public class SingolePool {
//    private static SingolePool singolePool = new SingolePool();
    private ThreadPoolExecutor threadPool;

    private static class SingoleInner {
        private static SingolePool singolePool = new SingolePool();
    }

    private SingolePool() {
        init();

    }

    private void init() {
        //三线程，2万任务，异常策略，10秒存活
        threadPool = new ThreadPoolExecutor(3,
                3,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(20000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    //获取单例的线程池
    public static ThreadPoolExecutor getInstance() {
//        return singolePool.threadPool;
        return SingoleInner.singolePool.threadPool;
    }
}
