package com.xcw.test;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @class: Test
 * @author: ChengweiXing
 * @description: TODO
 **/
@Slf4j
public class Test {

    private static final Long TIME_INTERVAL = 5000L;

    public static void main(String[] args) throws InterruptedException {
        List<Map<String,Object>> list=new ArrayList<>();
        HashMap<String, Object> a = new HashMap<>();
        a.put("aaa",123);
        a.put("bb",2);
        HashMap<String, Object> b = new HashMap<>();
        b.put("aaa",123);
        b.put("bb",2);
        list.add(b);
        list.add(a);
        System.out.println(a);
        System.out.println(list);
    }
}
