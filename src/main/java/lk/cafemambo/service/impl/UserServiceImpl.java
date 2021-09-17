package lk.cafemambo.service.impl;

import lk.cafemambo.dto.RegistrationDto;
import lk.cafemambo.dto.UserDto;
import lk.cafemambo.entity.LoginEntity;
import lk.cafemambo.entity.UserEntity;
import lk.cafemambo.repository.LoginRepository;
import lk.cafemambo.repository.UserRepository;
import lk.cafemambo.security.JwtTokenProvider;
import lk.cafemambo.service.UserService;
import lk.cafemambo.util.AppConstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     *
     * @param registrationDto
     * @return
     */
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

            return new ResponseEntity<>("200",HttpStatus.OK);

        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *
     * @param userDto
     * @return
     */
    @Override
    public ResponseEntity<?> save(UserDto userDto) {

        try {

            LoginEntity loginEntity = new LoginEntity();
            loginEntity.setId(UUID.randomUUID().toString());
            loginEntity.setEmail(userDto.getEmail());
            loginEntity.setLocked(false);
            loginEntity.setCreateBy(jwtTokenProvider.getUserEmailByRequestToken());
            loginEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
            loginEntity.setStatus(AppConstance.ACTIVE);

            loginEntity = loginRepository.save(loginEntity);

            UserEntity userEntity = new UserEntity();
            userEntity.setId(UUID.randomUUID().toString());
            userEntity.setRole(userDto.getRole());
            userEntity.setLoginEntity(loginEntity);
            userEntity.setName(userDto.getName());
            userEntity.setStatus(AppConstance.ACTIVE);
            userEntity.setEmail(userDto.getEmail());
            userEntity.setCreateDate(new Date());
            userEntity.setAddress(userDto.getAddress());
            userEntity.setTelNo(userDto.getTelNo());

            userRepository.save(userEntity);

            return new ResponseEntity<>("200",HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     *
     * @param userDto
     * @return
     */
    @Override
    public ResponseEntity<?> update(UserDto userDto) {


        try {

            UserEntity userEntity = userRepository.getById(userDto.getId());

            if(userEntity == null){
                return new ResponseEntity<>("Invalid User",HttpStatus.INTERNAL_SERVER_ERROR);
            }


            userEntity.setName(userDto.getName());

            if(!userEntity.getRole().equalsIgnoreCase(AppConstance.USER_ROLE)){
                userEntity.setRole(userDto.getRole());
            }

            userEntity.setAddress(userDto.getAddress());
            userEntity.setTelNo(userDto.getTelNo());

            userRepository.save(userEntity);

            return new ResponseEntity<>("200",HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }




    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public ResponseEntity<?> remove(String id) {

        try {

            UserEntity userEntity = userRepository.getById(id);

            if(userEntity == null){
                return new ResponseEntity<>("Invalid User",HttpStatus.INTERNAL_SERVER_ERROR);
            }


            userEntity.setStatus(AppConstance.INACTIVE);

            userRepository.save(userEntity);

            return new ResponseEntity<>("200",HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public ResponseEntity<?> gets(String id) {

        try {

            UserEntity userEntity = userRepository.getById(id);

            if(userEntity == null){
                return new ResponseEntity<>("Invalid User",HttpStatus.INTERNAL_SERVER_ERROR);
            }

            UserDto userDto = new UserDto();
            userDto.setName(userEntity.getName());
            userDto.setEmail(userEntity.getEmail());
            userDto.setId(userEntity.getId());
            userDto.setRole(userEntity.getRole());
            userDto.setAddress(userEntity.getAddress());
            userDto.setTelNo(userEntity.getTelNo());

            return new ResponseEntity<>(userDto,HttpStatus.OK);


        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> getAll() {
        try {

            List<UserEntity> userEntities = userRepository.findAllByStatus(AppConstance.ACTIVE);

            List<UserDto> userDtoList = new ArrayList<>();

            for (UserEntity userEntity : userEntities) {
                if(!userEntity.getRole().equalsIgnoreCase(AppConstance.TABLE_ROLE)){
                    UserDto userDto = new UserDto();
                    userDto.setEmail(userEntity.getEmail());
                    userDto.setId(userEntity.getId());
                    userDto.setRole(userEntity.getRole());
                    userDto.setAddress(userEntity.getAddress());
                    userDto.setTelNo(userEntity.getTelNo());
                    userDto.setName(userEntity.getName());
                    userDtoList.add(userDto);
                }

            }

            return new ResponseEntity<>(userDtoList,HttpStatus.OK);


        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
