package lk.cafemambo.repository;

import lk.cafemambo.entity.ShopTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopTableRepository extends JpaRepository<ShopTableEntity,String> {

    ShopTableEntity findByTableNumber(Integer tableNumber);

    List<ShopTableEntity> findAllByStatus(String active);
}
