package lk.cafemambo.repository;

import lk.cafemambo.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity,String> {
}
