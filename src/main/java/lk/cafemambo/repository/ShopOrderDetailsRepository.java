package lk.cafemambo.repository;

import lk.cafemambo.entity.ShopOrderDetailsEntity;
import lk.cafemambo.entity.ShopOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopOrderDetailsRepository extends JpaRepository<ShopOrderDetailsEntity,String> {
    List<ShopOrderDetailsEntity> findAllByStatusAndShopOrderEntity(String active, ShopOrderEntity shopOrderEntity);
}
