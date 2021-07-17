package com.imooc.distributedjob;

import com.google.gson.Gson;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.ShardingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class MyXxlJob {

    /**
     * 调度从数据库出发 进行触达
     * 1 单机串行 一个一个来 前面不执行完 后面不调度（刚执行完 就去执行未完成的 调度当场产生 但是需要等待才能执行）
     * 2 丢弃后续调度 存在运行就不执行 后面调度还是如此（不产生调度（调度记录当场失败） 后续空闲才产生）
     * 3 覆盖之前 取消正在执行中的之前的调度 （打断线程 interruptedExcetion）
     */
    @XxlJob("myXxlJobHandler")
    public ReturnT<String> execute(String param) {
        log.info("myXxlJobHandler execute...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //轮询 流量均摊，推荐
        //故障转移 流量到第一台，查日志方便

        //分片广播 全部节点都会收到
        //从数据库里查询所有用户，为每一个用户生成结单
        //10w用户，2台服务器，每台运算5w用户数据，耗时减半
        ShardingUtil.ShardingVO shardingVo = ShardingUtil.getShardingVo();

        //分片，必须掌握
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        for (Integer i : list) {
            //数据下标取模总分片，等于当前分片才进行执行
            if (i % shardingVo.getTotal() == shardingVo.getIndex()) {
                log.info("myXxlJobHandler execute...user={}. shardingVo={}", i, new Gson().toJson(shardingVo));
            }
        }
        //返回到xxl 日志
        XxlJobLogger.log("myXxlJobHandler execute...");
        return ReturnT.SUCCESS;
    }
}
