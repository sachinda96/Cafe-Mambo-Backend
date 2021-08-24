package lk.cafemambo.service;

import lk.cafemambo.dto.RegistrationDto;
import lk.cafemambo.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface UserService {

    public ResponseEntity<?> registration(RegistrationDto registrationDto);

    public ResponseEntity<?> save(UserDto userDto);

    public ResponseEntity<?> update(UserDto userDto);

    public ResponseEntity<?> remove(String id);

    public ResponseEntity<?> gets(String id);

    public ResponseEntity<?> getAll();

}
