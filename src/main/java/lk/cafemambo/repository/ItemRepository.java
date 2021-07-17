package lk.cafemambo.repository;

import lk.cafemambo.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity,String> {
}
