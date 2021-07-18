package com.imooc.seataorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableFeignClients
public class SeataOrderApplication {

    /**
     * 分布式事务
     * seata 分为四种种模式
     * TCC -》 try confirm/cancel 性能高 缺点 需要实现 确认和取消
     * AT -》 记录前后数据快照 比较快照数据 进行回滚（执行完成会删除快照）
     * SEGA -> 回滚冲正 实现冲正接口 优点 无锁高性能 不保证数据隔离
     * XA -> 使用两阶段提交,来保证所有资源同时提交或回滚任何特定的事务
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(SeataOrderApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
