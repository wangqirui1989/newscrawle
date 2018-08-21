package com.zjs.newscrawle.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: Qirui Wang
 * @Description: 多线程配置
 * @Date: 11/8/18
 */
@Configuration
public class TaskExecutorConfig implements AsyncConfigurer {

    @Value("${taskExecutor.corePoolSize}")
    private int corePoolSize;

    @Value("${taskExecutor.maxPoolSize}")
    private int maxPoolSize;

    @Value("${taskExecutor.queueCapacity}")
    private int queueCapacity;

    @Value("${taskExecutor.threadNamePrefix}")
    private String threadNamePrefix;

    public int getCorePoolSize() {
        return corePoolSize;
    }

    /**
     *
     * @author Qirui Wang
     * @date 13/8/18 20:35
     * @usage 设置线程池
     * @method getAsyncExecutor
     * @param
     * @return java.util.concurrent.Executor
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        threadPoolExecutor.setCorePoolSize(corePoolSize);
        threadPoolExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolExecutor.setQueueCapacity(queueCapacity);
        threadPoolExecutor.setThreadNamePrefix(threadNamePrefix);
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        threadPoolExecutor.initialize();

        return threadPoolExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
