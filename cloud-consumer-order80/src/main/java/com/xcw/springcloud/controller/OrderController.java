package com.xcw.springcloud.controller;

import com.xcw.springcloud.entities.CommonResult;
import com.xcw.springcloud.entities.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @class: OrderController
 * @author: ChengweiXing
 * @description: TODO
 **/
@RestController
public class OrderController {

    //这里不能写死，可能调用不同的微服务
//    public final static String paymentURL="http://localhost:8001/payment";
    public final static String paymentURL="http://cloud-payment-service";

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 订单调用支付业务，通过http调用，创建订单
     * @param payment
     * @return
     */
    @GetMapping("/consumer/payment/create")
    public CommonResult<Payment> create(Payment  payment){
        return restTemplate.postForObject(paymentURL+"/payment/create",payment,CommonResult.class);
    }

    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult<Payment> get(@PathVariable("id") Long id){
        return restTemplate.getForObject(paymentURL+"/payment/get/"+id,CommonResult.class);
    }


}
