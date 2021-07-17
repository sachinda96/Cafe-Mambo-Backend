package lk.cafemambo.repository;

import lk.cafemambo.entity.OrderDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetailsEntity,String> {
}
