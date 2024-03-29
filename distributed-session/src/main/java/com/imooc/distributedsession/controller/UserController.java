package com.imooc.distributedsession.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.imooc.distributedsession.LoginIntercepter.JWT_KEY;
import static com.imooc.distributedsession.LoginIntercepter.UID;

@RequestMapping("/user")
@RestController
public class UserController {

    /**
     * 分布式session
     * 1 spring session 使用方式和速配那个
     * 2 redis + token
     * 3 jwt
     */

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session){
        //账号密码正确
        session.setAttribute("login_user", username);
        return "登录成功";
    }

    @GetMapping("/info")
    public String info(HttpSession session) {
        return "当前登录的是：" + session.getAttribute("login_user");
    }

    @GetMapping("/loginWithToken")
    public String loginWithToken(@RequestParam String username,
                                 @RequestParam String password) {
        //账号密码正确
        String key = "token_" + UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(key, username, 3600, TimeUnit.SECONDS);
        return key;
    }

    @GetMapping("/infoWithToken")
    public String infoWithToken(@RequestHeader String token) {
        return "当前登录的是：" + stringRedisTemplate.opsForValue().get(token);
    }

    @GetMapping("/loginWithJwt")
    public String loginWithJwt(@RequestParam String username,
                               @RequestParam String password) {
        //获取密码 （通过加密key JWT_KEY 获取）
        Algorithm algorithm = Algorithm.HMAC256(JWT_KEY);
        //创建token
        String token = JWT.create()
                //指定字段
                .withClaim("login_user", username)
                .withClaim(UID, 1)
                //指定过期时间
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                .sign(algorithm);
        return token;
    }


    @GetMapping("/infoWithJwt")
    public String infoWithJwt(@RequestAttribute String token) {
        //获取密码 （通过加密key JWT_KEY 获取）
        Algorithm algorithm = Algorithm.HMAC256(JWT_KEY);
        //获取解密对象
        JWTVerifier verifier = JWT.require(algorithm).build();
        //解密
        DecodedJWT decodedJWT = verifier.verify(token);
        //返回数据体
        return decodedJWT.getPayload();
    }


    //获取地址, token -> id
    @GetMapping("/address")
    public Integer address(@RequestAttribute Integer uid) {
        return uid;
    }

    //修改地址....
}
