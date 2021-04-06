package com.xcw.springcloud.dao;

/**
 * @class: PaymentDao
 * @author: ChengweiXing
 * @description: TODO
 **/
import com.xcw.springcloud.entities.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 */
@Mapper
//@Repository不用Spring的
public interface PaymentDao
{
    public int create(Payment payment);

    public Payment getPaymentById(@Param("id") Long id);
}
