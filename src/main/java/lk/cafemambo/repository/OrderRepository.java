package lk.cafemambo.repository;

import lk.cafemambo.entity.OrderEntity;
import lk.cafemambo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity,String> {

    List<OrderEntity> findAllByOrderStatusAndStatus(String orderStatusPending, String active);

    List<OrderEntity> findAllByUserEntityAndStatus(UserEntity userEntity, String active);

    List<OrderEntity> findAllByStatus(String active);
}
