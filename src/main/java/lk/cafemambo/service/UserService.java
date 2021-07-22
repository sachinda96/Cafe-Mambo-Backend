package lk.cafemambo.service;

import lk.cafemambo.dto.RegistrationDto;
import org.springframework.http.ResponseEntity;

public interface UserService {

    public ResponseEntity<?> registration(RegistrationDto registrationDto);
}
