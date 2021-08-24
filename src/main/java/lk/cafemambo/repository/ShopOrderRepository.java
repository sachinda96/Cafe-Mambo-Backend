package lk.cafemambo.repository;

import lk.cafemambo.entity.ShopOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopOrderRepository extends JpaRepository<ShopOrderEntity,String> {
    List<ShopOrderEntity> findAllByLevelAndStatus(String level, String active);

}
