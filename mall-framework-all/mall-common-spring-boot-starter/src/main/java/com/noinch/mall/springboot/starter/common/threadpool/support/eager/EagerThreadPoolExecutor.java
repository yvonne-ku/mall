
package com.noinch.mall.springboot.starter.common.threadpool.support.eager;


import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 快速消费线程池
 *
 * 饥饿线程池的工作逻辑：
 * 1. 任务提交时，若核心线程不满，交给核心线程处理
 * 2. 任务提交时，若核心线程满，但最大线程不满，创建非核心线程处理
 * 3. 任务提交时，若核心线程和最大线程都满，交给队列等待将来处理
 * 4. 任务提交时，若队列也满了，触发拒绝策略处理
 *
 * 这个工作逻辑是在 TaskQueue 的 offer 方法上实现的。结合 jdk 的 ThreadPoolExecutor 内部源码对 offer 方法的调用和反馈。
 */
public class EagerThreadPoolExecutor extends ThreadPoolExecutor {

    public EagerThreadPoolExecutor(int corePoolSize,
                                   int maximumPoolSize,
                                   long keepAliveTime,
                                   TimeUnit unit,
                                   TaskQueue<Runnable> workQueue,
                                   ThreadFactory threadFactory,
                                   RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }
    
    private final AtomicInteger submittedTaskCount = new AtomicInteger(0);
    
    public int getSubmittedTaskCount() {
        return submittedTaskCount.get();
    }
    
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        submittedTaskCount.decrementAndGet();
    }
    
    @Override
    public void execute(Runnable command) {
        submittedTaskCount.incrementAndGet();
        try {
            // 调用线程池的原始 execute，配合自定义的 TaskQueue
            // 1. 如果 corePoolSize 没满，offer 到 taskQueue 返回 true，交给核心线程处理
            // 2. 如果 corePoolSize 满了，offer 到 taskQueue 返回 false，强制创建非核心线程
            // 3. 如果 maximumPoolSize 满了，offer 到 taskQueue 返回 true，等待处理
            // 4. 如果 maximumPoolSize 满了，offer 到 taskQueue 返回 false，队列已满，拒绝处理策略
            super.execute(command);
        } catch (RejectedExecutionException ex) {
            // 触发拒绝策略执行时，尝试将任务重新入队
            // 若入队成功，任务会被其他线程执行
            // 若入队失败，任务会被拒绝处理策略处理
            TaskQueue taskQueue = (TaskQueue) super.getQueue();
            try {
                if (!taskQueue.retryOffer(command, 0, TimeUnit.MILLISECONDS)) {
                    submittedTaskCount.decrementAndGet();
                    throw new RejectedExecutionException("Queue capacity is full.", ex);
                }
            } catch (InterruptedException iex) {
                submittedTaskCount.decrementAndGet();
                throw new RejectedExecutionException("Thread interrupted while trying to re-offer task to queue.", iex);
            }
        } catch (Exception ex) {
            submittedTaskCount.decrementAndGet();
            throw ex;
        }
    }
}
