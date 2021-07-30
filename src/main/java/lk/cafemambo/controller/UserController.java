package lk.cafemambo.controller;

import lk.cafemambo.dto.RegistrationDto;
import lk.cafemambo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/registration",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registration(@RequestBody RegistrationDto registrationDto){
        return  userService.registration(registrationDto);
    }

}
