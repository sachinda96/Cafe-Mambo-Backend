package lk.cafemambo.repository;

import lk.cafemambo.entity.LoginEntity;
import lk.cafemambo.util.AppConstance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<LoginEntity,String> {

    LoginEntity findByEmail(String email)throws Exception;

    LoginEntity findByEmailAndStatus(String email, String status);
}
