package lk.cafemambo.repository;

import lk.cafemambo.entity.LoginEntity;
import lk.cafemambo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity,String> {
    UserEntity findByEmail(String email)throws Exception;

    UserEntity findByLoginEntityAndStatus(LoginEntity loginEntity, String active);

    UserEntity findByNameAndRole(String tableId, String tableRole);

    List<UserEntity> findAllByStatus(String active);

    UserEntity getByIdAndStatus(String customerId, String active);
}
