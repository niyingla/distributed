package com.imooc.distributedjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
//全局开启定时 @Scheduled
@EnableScheduling
//开启异步
@EnableAsync
public class DistributedJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedJobApplication.class, args);
    }

    /**
     * 防止task @Scheduled 单线程执行
     * 此处设置线程池
     * @return
     */
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        return taskScheduler;
    }
}
