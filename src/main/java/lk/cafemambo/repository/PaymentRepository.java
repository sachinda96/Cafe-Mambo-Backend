package lk.cafemambo.repository;

import lk.cafemambo.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity,String>{

}
