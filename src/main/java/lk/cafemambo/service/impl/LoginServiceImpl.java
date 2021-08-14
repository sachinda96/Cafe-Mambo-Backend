package lk.cafemambo.service.impl;

import lk.cafemambo.dto.LoginDto;
import lk.cafemambo.entity.LoginEntity;
import lk.cafemambo.repository.LoginRepository;
import lk.cafemambo.security.JwtTokenProvider;
import lk.cafemambo.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseEntity<?> login(LoginDto loginDto) {

        try {

            LoginEntity loginEntity = loginRepository.findByEmail(loginDto.getEmail());

            if(loginEntity != null){

                if(passwordEncoder.matches(loginDto.getPassword(),loginEntity.getPassword())){
                    System.out.println(jwtTokenProvider.createToken(loginEntity.getEmail()));
                    return new ResponseEntity<>(jwtTokenProvider.createToken(loginEntity.getEmail()),HttpStatus.OK);
                }

            }

            return new ResponseEntity<>("Invalid credentials please check username or password",HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
