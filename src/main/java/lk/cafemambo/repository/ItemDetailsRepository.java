package lk.cafemambo.repository;

import lk.cafemambo.entity.ItemDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemDetailsRepository extends JpaRepository<ItemDetailsEntity,String> {
}
