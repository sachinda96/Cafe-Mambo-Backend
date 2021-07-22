package lk.cafemambo.controller;

import lk.cafemambo.dto.LoginDto;
import lk.cafemambo.entity.LoginEntity;
import lk.cafemambo.repository.LoginRepository;
import lk.cafemambo.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        return loginService.login(loginDto);
    }

}
