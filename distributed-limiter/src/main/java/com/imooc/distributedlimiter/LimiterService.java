package com.imooc.distributedlimiter;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Service;

@Service
public class LimiterService {

    //取个名字
    @SentinelResource("LimiterService.process")
    //自定义指定 需要是好像静态
//    @SentinelResource(blockHandler = "handException",blockHandlerClass = LimiterBlockHandler.class)
    public String process() {
        return "process";
    }
}
