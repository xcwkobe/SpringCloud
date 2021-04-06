package com.xcw.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

/**
 * @class: JWTUtil
 * @author: ChengweiXing
 * @description: TODO
 **/
public class JWTUtil {

    private final static String secret="token!Q@W#E$R";

    /**
     * 构建token，在payload中加入一些信息
     * @param map
     * @return
     */
    public static String getToken(Map<String ,String> map){
        JWTCreator.Builder builder = JWT.create();

        //payload
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE,7);//默认7天过期

        builder.withExpiresAt(instance.getTime());
        String token = builder.sign(Algorithm.HMAC256(secret));
        return token;
    }

    /**
     * 验证token，有问题会抛出异常
     * @param token
     * @return
     */
    public static DecodedJWT verify(String token){
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
        return verify;
    }
}
