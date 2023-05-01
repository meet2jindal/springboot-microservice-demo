package com.example.orderservice.service;

import com.example.orderservice.common.Payment;
import com.example.orderservice.common.TransactionRequest;
import com.example.orderservice.common.TransactionResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "paymentServiceDown")
    public TransactionResponse saveOrder(TransactionRequest request){
        String orderReponse;
        Order order = request.getOrder();
        Payment payment = request.getPayment() != null ? request.getPayment() : new Payment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getPrice());

        Payment paymentReponse = restTemplate.postForObject("http://PAYMENT-SERVICE/payment/doPayment", payment, Payment.class);
        orderReponse = paymentReponse.getPaymentStatus().equals("success")? "Order success" : "order fail";
        orderRepository.save(order);
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setOrder(order);
        transactionResponse.setAmount(order.getPrice());
        transactionResponse.setTransactionId(paymentReponse.getTransactionId());
        transactionResponse.setOrderStatus(orderReponse);
        return transactionResponse;
    }

    public TransactionResponse paymentServiceDown(TransactionRequest request){

        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setOrderStatus("Failed- Payment service unavailable");
        transactionResponse.setOrder(request.getOrder());
        transactionResponse.setTransactionId(null);
        return transactionResponse;
    }
}
