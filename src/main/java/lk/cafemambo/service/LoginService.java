package lk.cafemambo.service;

import lk.cafemambo.dto.LoginDto;
import org.springframework.http.ResponseEntity;

public interface LoginService {

    public ResponseEntity<?> login(LoginDto loginDto);
}
