package com.xcw.springcloud.service;

import com.xcw.springcloud.entities.Payment;
import org.apache.ibatis.annotations.Param;

/**
 * @class: PaymentService
 * @author: ChengweiXing
 * @description: TODO
 **/
public interface PaymentService
{
    public int create(Payment payment);

    public Payment getPaymentById(@Param("id") Long id);
}
