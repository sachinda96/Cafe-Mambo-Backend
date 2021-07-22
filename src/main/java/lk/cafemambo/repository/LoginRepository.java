package lk.cafemambo.repository;

import lk.cafemambo.entity.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<LoginEntity,String> {

    LoginEntity findByEmail(String email)throws Exception;
}
