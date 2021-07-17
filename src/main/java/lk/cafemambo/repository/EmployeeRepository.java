package lk.cafemambo.repository;

import lk.cafemambo.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity,String> {
}
