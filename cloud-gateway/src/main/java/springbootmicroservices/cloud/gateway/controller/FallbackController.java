package springbootmicroservices.cloud.gateway.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController

public class FallbackController {

    @GetMapping("/orderFallback")
    public Mono<String> orderFallback() {
        return Mono.just("Order service is not available. Please try after some time.");
    }

    @GetMapping("/paymentFallback")
    public Mono<String> paymentFallback() {
        return Mono.just("Payment service is not available. Please try after some time.");
    }
}
