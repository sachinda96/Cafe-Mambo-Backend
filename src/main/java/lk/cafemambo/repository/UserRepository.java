package lk.cafemambo.repository;

import lk.cafemambo.entity.LoginEntity;
import lk.cafemambo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,String> {
    UserEntity findByEmail(String email)throws Exception;

    UserEntity findByLoginEntityAndStatus(LoginEntity loginEntity, String active);
}
