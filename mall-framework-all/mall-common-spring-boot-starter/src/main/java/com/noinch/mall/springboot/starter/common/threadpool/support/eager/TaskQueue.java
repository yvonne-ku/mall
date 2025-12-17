
package com.noinch.mall.springboot.starter.common.threadpool.support.eager;

import lombok.Setter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 快速消费任务队列
 * */
public class TaskQueue<R extends Runnable> extends LinkedBlockingQueue<Runnable> {

    @Setter
    private EagerThreadPoolExecutor executor;

    public TaskQueue(int capacity) {
        super(capacity);
    }

    @Override
    public boolean offer(Runnable runnable) {
        int currentPoolThreadSize = executor.getPoolSize();
        // 1. 有核心线程空闲，将任务加入阻塞队列，返回 true，由核心线程进行处理任务
        if (executor.getSubmittedTaskCount() < currentPoolThreadSize) {
            return super.offer(runnable);
        }
        // 2. 无核心线程空闲，返回 False，根据线程池源码，当 offer 返回 False 时会创建非核心线程
        if (currentPoolThreadSize < executor.getMaximumPoolSize()) {
            return false;
        }
        // 3. 超过最大线程数，任务加入阻塞队列，返回 true
        return super.offer(runnable);
    }

    public boolean retryOffer(Runnable o, long timeout, TimeUnit unit) throws InterruptedException {
        if (executor.isShutdown()) {
            throw new RejectedExecutionException("Executor is shutdown!");
        }
        return super.offer(o, timeout, unit);
    }
}
