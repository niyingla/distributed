package com.imooc.distributedlimiter;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
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

//        //指定流控规则存储数据源
//        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new Nac<>(remoteAddress, path, source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
//        //写入流控管理
//        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());

        //指定nacos 读取规则
        ReadableDataSource<String, List<ParamFlowRule>> paramRuleSource = new NacosDataSource<>(remoteAddress, "groupId",
                "paramDataId", source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {}));
        ParamFlowRuleManager.register2Property(paramRuleSource.getProperty());
    }
}
