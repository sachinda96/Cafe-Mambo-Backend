package lk.cafemambo.repository;

import lk.cafemambo.entity.CategoryEntity;
import lk.cafemambo.entity.ItemEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity,String> {
    List<ItemEntity> findAllByStatus(String active)throws Exception;

    ItemEntity findByIdAndStatus(String id, String active)throws Exception;

    List<ItemEntity> findAllByCategoryEntityAndStatus(CategoryEntity categoryEntity,String status)throws Exception;

    List<ItemEntity> findAllByCategoryEntityAndStatus(CategoryEntity categoryEntity,String Status, Pageable pageable)throws Exception;

    List<ItemEntity> findAllByStatus(String status,Pageable pageable)throws Exception;

    long countByCategoryEntityAndStatus(CategoryEntity categoryEntity, String active);
}
