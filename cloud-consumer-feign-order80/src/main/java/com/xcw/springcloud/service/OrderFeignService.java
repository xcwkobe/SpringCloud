package com.xcw.springcloud.service;

import com.xcw.springcloud.entities.CommonResult;
import com.xcw.springcloud.entities.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @class: OrderFeignService
 * @author: ChengweiXing
 * @description: TODO
 **/
@Component
@FeignClient(value = "CLOUD-PAYMENT-SERVICE")
public interface OrderFeignService {

    /**
     * feignclient直接通过application name调用微服务群的controller
     * @param id
     * @return
     */
    @GetMapping("/payment/get/{id}")
    CommonResult<Payment> get(@PathVariable("id") Long id);
}
