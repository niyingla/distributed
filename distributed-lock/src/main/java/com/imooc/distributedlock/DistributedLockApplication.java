package com.imooc.distributedlock;

import org.redisson.RedissonLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@SpringBootApplication
public class DistributedLockApplication {

    @Autowired
    private RedissonClient redissonClient;

    public static void main(String[] args) {
        SpringApplication.run(DistributedLockApplication.class, args);
    }

    @GetMapping("/lock")
    public void lock() {
        RLock lock = redissonClient.getLock("lock");
        try {
            // key:hash=value
            // lock:链接id+线程id: value 来做到重入
            //超时 5s
            lock.lock(5, TimeUnit.SECONDS);
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //1、lock.isFair()方法用来判断lock锁是公平锁还是非公平锁。公平锁是指，线程获得锁的顺序是按其等待锁的先后顺序来的，
            // 先来先获得FIFO。反之，非公平锁则是线程随机获得锁的，lock默认是非公平锁。
            //2、lock.isHeldByCurrentThread()的作用是查询当前线程是否保持此锁定，和lock.hasQueueThread()不同的地方是：
            // lock.hasQueueThread(Thread thread)的作用是判断当前线程是否处于等待lock的状态。
            //3、lock.isLocked()的作用是查询此锁定是否由任意线程保持。
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
            System.out.println("解除锁定了");
        }
    }
}
