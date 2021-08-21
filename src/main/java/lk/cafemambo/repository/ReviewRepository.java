package lk.cafemambo.repository;

import lk.cafemambo.entity.ItemEntity;
import lk.cafemambo.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity,String> {

    List<ReviewEntity> findAllByItemEntityAndStatus(ItemEntity itemEntity, String active);
}
