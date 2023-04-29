package springboot.microservice.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.microservice.payment.entrity.Payment;
import springboot.microservice.payment.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("doPayment")
    public Payment doPayment(@RequestBody Payment payment){
        return paymentService.doPayment(payment);

    }
}
