package lk.cafemambo.repository;

import lk.cafemambo.entity.OrderDetailsEntity;
import lk.cafemambo.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetailsEntity,String> {
    List<OrderDetailsEntity> findAllByStatusAndOrderEntity(String active, OrderEntity orderEntity);
}
