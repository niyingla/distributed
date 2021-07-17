package com.imooc.distributedlimiter;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

//如何返回json, springboot全局异常处理
@Slf4j
@Component
public class LimiterBlockHandler implements BlockExceptionHandler {

    /**
     * 自定义限流异常响应处理类
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @throws Exception
     */

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {

        log.info("被限流了...");

        //1 可以直接写出返回
        PrintWriter out = httpServletResponse.getWriter();
        out.print("啦啦啦啦啦啦");
        out.flush();
        out.close();

        //2 也可以抛出异常BlockException 让全局异常返回
        throw e;
    }

    public static void handException(BlockException e){
        System.out.println("啦啦啦啦啦啦");
    }
}
