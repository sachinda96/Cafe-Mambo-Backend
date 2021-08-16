package lk.cafemambo.service.impl;

import lk.cafemambo.dto.LoginDto;
import lk.cafemambo.dto.TokenDto;
import lk.cafemambo.entity.LoginEntity;
import lk.cafemambo.entity.UserEntity;
import lk.cafemambo.repository.LoginRepository;
import lk.cafemambo.repository.UserRepository;
import lk.cafemambo.security.JwtTokenProvider;
import lk.cafemambo.service.LoginService;
import lk.cafemambo.util.AppConstance;
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
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseEntity<?> login(LoginDto loginDto) {

        try {

            LoginEntity loginEntity = loginRepository.findByEmail(loginDto.getEmail());

            if(loginEntity != null){
                UserEntity userEntity = userRepository.findByLoginEntityAndStatus(loginEntity, AppConstance.ACTIVE);

                if(passwordEncoder.matches(loginDto.getPassword(),loginEntity.getPassword())){

                    TokenDto tokenDto = new TokenDto();
                    tokenDto.setToken(jwtTokenProvider.createToken(loginEntity.getEmail()));
                    tokenDto.setUserId(userEntity.getId());

                    return new ResponseEntity<>(tokenDto,HttpStatus.OK);
                }

            }

            return new ResponseEntity<>("Invalid credentials please check username or password",HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
