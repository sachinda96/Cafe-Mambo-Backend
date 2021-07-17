package lk.cafemambo.repository;

import lk.cafemambo.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity,String> {
}
