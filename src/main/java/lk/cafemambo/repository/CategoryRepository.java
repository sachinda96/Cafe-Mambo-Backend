package lk.cafemambo.repository;

import lk.cafemambo.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity,String> {
    List<CategoryEntity> findAllByStatus(String active);

    CategoryEntity getByIdAndStatus(String id,String Status);
}
