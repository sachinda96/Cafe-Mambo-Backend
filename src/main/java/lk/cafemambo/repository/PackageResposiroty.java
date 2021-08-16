package lk.cafemambo.repository;

import lk.cafemambo.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageResposiroty extends JpaRepository<PackageEntity,String> {
    List<PackageEntity> findAllByStatus(String active);
}
