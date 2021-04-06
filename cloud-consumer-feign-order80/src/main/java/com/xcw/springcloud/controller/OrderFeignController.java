package com.xcw.springcloud.controller;

import com.xcw.springcloud.entities.CommonResult;
import com.xcw.springcloud.entities.Payment;
import com.xcw.springcloud.service.OrderFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @class: OrderFeignController
 * @author: ChengweiXing
 * @description: TODO
 **/
@RestController
@Slf4j
public class OrderFeignController {

    @Autowired
    private OrderFeignService orderFeignService;

    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult<Payment> get(@PathVariable("id") Long id){
        return orderFeignService.get(id);
    }
}
