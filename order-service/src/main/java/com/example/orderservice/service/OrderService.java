package com.example.orderservice.service;

import com.example.orderservice.common.Payment;
import com.example.orderservice.common.TransactionRequest;
import com.example.orderservice.common.TransactionResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RefreshScope
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    @Value("${microservice.payment-service.endpoints.endpoint.uri}")
    private String PAYMENT_SERVICE_ENDPOINT;

    @HystrixCommand(fallbackMethod = "paymentServiceDown")
    public TransactionResponse saveOrder(TransactionRequest request) {
        String orderReponse;
        Order order = request.getOrder();

        LOGGER.info("Order Request " + order);
        Payment payment = request.getPayment() != null ? request.getPayment() : new Payment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getPrice());

        Payment paymentResponse = restTemplate.postForObject(PAYMENT_SERVICE_ENDPOINT, payment, Payment.class);
        LOGGER.info("Paymnet Response for rest call from order service " + paymentResponse);
        orderReponse = paymentResponse != null && paymentResponse.getPaymentStatus().equals("success") ? "Order success" : "order fail";

        orderRepository.save(order);
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setOrder(order);
        transactionResponse.setAmount(order.getPrice());
        transactionResponse.setTransactionId(paymentResponse != null ? paymentResponse.getTransactionId() : null);
        transactionResponse.setOrderStatus(orderReponse);
        return transactionResponse;
    }

    public TransactionResponse paymentServiceDown(TransactionRequest request) {

        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setOrderStatus("Failed- Payment service unavailable");
        transactionResponse.setOrder(request.getOrder());
        transactionResponse.setTransactionId(null);
        return transactionResponse;
    }
}
