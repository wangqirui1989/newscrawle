package com.zjs.newscrawle.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @Author: Qirui Wang
 * @Description: 多线程配置
 * @Date: 11/8/18
 */
@Configuration
public class TaskExecutorConfig implements AsyncConfigurer {

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
        threadPoolExecutor.setCorePoolSize(5);
        threadPoolExecutor.setMaxPoolSize(10);
        threadPoolExecutor.setQueueCapacity(25);

        threadPoolExecutor.initialize();

        return threadPoolExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
