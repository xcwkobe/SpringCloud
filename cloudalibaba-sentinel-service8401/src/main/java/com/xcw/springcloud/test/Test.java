package com.xcw.springcloud.test;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

/**
 * @class: Test
 * @author: ChengweiXing
 * @description: TODO
 **/
@Slf4j
public class Test {

    public static void main(String[] args) {
        System.out.println(Instant.now());
        log.info("success");
    }
}
