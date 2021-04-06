package com.xcw.springcloud.service.serviceImp;

import com.xcw.springcloud.dao.PaymentDao;
import com.xcw.springcloud.entities.Payment;
import com.xcw.springcloud.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @class: PaymentServiceImp
 * @author: ChengweiXing
 * @description: TODO
 **/

@Service
public class PaymentServiceImp implements PaymentService
{
    @Autowired
    private PaymentDao paymentDao;

    public int create(Payment payment)
    {
        return paymentDao.create(payment);
    }

    public Payment getPaymentById(Long id)
    {
        return paymentDao.getPaymentById(id);
    }
}
