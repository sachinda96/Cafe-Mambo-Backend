package lk.cafemambo.repository;

import lk.cafemambo.entity.PackageDetailsEntity;
import lk.cafemambo.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageDetailsRepository extends JpaRepository<PackageDetailsEntity,String> {

    List<PackageDetailsEntity> findAllByPackageEntityAndStatus(PackageEntity packageEntity, String active);
}
