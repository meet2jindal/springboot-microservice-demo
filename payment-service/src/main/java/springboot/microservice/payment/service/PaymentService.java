package springboot.microservice.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.microservice.payment.entrity.Payment;
import springboot.microservice.payment.repository.PaymentRepository;

import java.util.Random;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment doPayment(Payment payment) {
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setPaymentStatus(paymentProcessing());

        return paymentRepository.save(payment);
    }

    private String paymentProcessing() {
        return new Random().nextBoolean() ? "success" : "false";
    }

    public Payment findPaymentHistoryByOrderId(int orderId) {
        return paymentRepository.findById(orderId).orElse(null);
    }
}
