package com.imooc.distributedjob;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyQuartJobConfig {

    /**
     * 会把定时任务信息持久化到数据库
     * 来做到分布式调度
     */


    /**
     * 定义job详情
     * @return
     */
    @Bean
    public JobDetail jobDetail() {
        JobDetail detail = JobBuilder.newJob(MyQuartzJob.class)
                .withIdentity("job1", "group1")
                .storeDurably()
                .build();
        return detail;
    }

    /**
     * 定义触发器
     * @return
     */
    @Bean
    public Trigger trigger() {
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                //身份标识
                .withIdentity("trigger1", "group1")
                //现在开始 也可以指定时间
                .startNow()
                //指定表达式
                .withSchedule(CronScheduleBuilder.cronSchedule("0,5 * * * * ?"))
                .build();
        return trigger;
    }
}
