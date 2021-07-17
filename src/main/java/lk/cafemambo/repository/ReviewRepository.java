package lk.cafemambo.repository;

import lk.cafemambo.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity,String> {
}
