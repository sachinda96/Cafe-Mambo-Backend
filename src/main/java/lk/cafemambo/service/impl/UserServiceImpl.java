package lk.cafemambo.service.impl;

import lk.cafemambo.dto.RegistrationDto;
import lk.cafemambo.entity.LoginEntity;
import lk.cafemambo.entity.UserEntity;
import lk.cafemambo.repository.LoginRepository;
import lk.cafemambo.repository.UserRepository;
import lk.cafemambo.service.UserService;
import lk.cafemambo.util.AppConstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> registration(RegistrationDto registrationDto) {

        try {

            if(userRepository.findByEmail(registrationDto.getEmail()) != null){
                return new ResponseEntity<>("This Email Already Registered",HttpStatus.NO_CONTENT);
            }

            LoginEntity loginEntity = new LoginEntity();
            loginEntity.setId(UUID.randomUUID().toString());
            loginEntity.setEmail(registrationDto.getEmail());
            loginEntity.setLocked(false);
            loginEntity.setCreateBy(registrationDto.getEmail());
            loginEntity.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
            loginEntity.setStatus(AppConstance.ACTIVE);

            loginRepository.save(loginEntity);

            UserEntity userEntity = new UserEntity();
            userEntity.setRole(AppConstance.USER_ROLE);
            userEntity.setCreateBy(registrationDto.getEmail());
            userEntity.setId(UUID.randomUUID().toString());
            userEntity.setCreateDate(new Date());
            userEntity.setStatus(AppConstance.ACTIVE);
            userEntity.setEmail(registrationDto.getEmail());
            userEntity.setName(registrationDto.getName());
            userEntity.setLoginEntity(loginEntity);

            userRepository.save(userEntity);

            return new ResponseEntity<>("Success",HttpStatus.OK);

        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
