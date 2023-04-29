package springboot.microservice.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboot.microservice.payment.entrity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
