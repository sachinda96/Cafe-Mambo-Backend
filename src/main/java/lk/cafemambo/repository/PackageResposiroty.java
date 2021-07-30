package lk.cafemambo.repository;

import lk.cafemambo.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageResposiroty extends JpaRepository<PackageEntity,String> {
}
