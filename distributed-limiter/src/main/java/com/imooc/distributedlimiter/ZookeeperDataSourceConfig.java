package com.imooc.distributedlimiter;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ZookeeperDataSourceConfig {

    /**
     * nacos 和 zookeeper都可以作为存储件
     */
    @Bean
    public void loadRules() {
        //zookeeper地址
        final String remoteAddress = "zk.springboot.cn:2181";
        //存储规则地址
        final String path = "/limiter/sentinel-flow-rules";

        //指定流控规则存储数据源
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new ZookeeperDataSource<>(remoteAddress, path, source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
        //写入流控管理
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
    }
}
