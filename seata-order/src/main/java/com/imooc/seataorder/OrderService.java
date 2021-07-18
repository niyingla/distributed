package com.imooc.seataorder;

import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class OrderService {

    //Feign
    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private RestTemplate restTemplate;

    @GlobalTransactional
    public Boolean create(Integer count) {
        //1 调用product 扣库存 注意不可以new RestTemplate
        String url = "http://localhost:8086/deduct?productId=5001&count=" + count;
        Boolean result = restTemplate.getForObject(url, Boolean.class);

        //2 调用product 扣库存
        Boolean result1 = productFeignClient.deduct(5001L, count);
        if (result1 != null && result1) {
            //可能抛出异常
            if (5 == count) {
                throw new RuntimeException("order发生异常了");
            }

            log.info("数据库开始创建订单");
            return true;
        }

        return false;
    }
}
