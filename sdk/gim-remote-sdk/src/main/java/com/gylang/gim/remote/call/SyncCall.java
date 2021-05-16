package com.gylang.gim.remote.call;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 同步器
 *
 * @author gylang
 * data 2021/5/16
 */
public class SyncCall<T> implements GimCallBack<T> {

    private final GimCallBack<T> work;

    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    public SyncCall(GimCallBack<T> work) {
        this.work = work;
    }

    @Override
    public void call(T t) {
        work.call(t);
        countDownLatch.countDown();
    }

    public void await() throws InterruptedException {
        countDownLatch.await();

    }

    public void await(long milliseconds) throws InterruptedException {
        countDownLatch.await(milliseconds, TimeUnit.MILLISECONDS);
    }
}
