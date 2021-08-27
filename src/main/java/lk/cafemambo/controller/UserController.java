package lk.cafemambo.controller;

import lk.cafemambo.dto.RegistrationDto;
import lk.cafemambo.dto.UserDto;
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

    @PostMapping(value = "/save")
    public ResponseEntity<?> save(@RequestBody UserDto userDto){
        return userService.save(userDto);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<?> update(@RequestBody UserDto userDto){
        return userService.update(userDto);
    }

    @GetMapping(value = "/remove/{id}")
    public ResponseEntity<?> remove(@PathVariable String id){
        return userService.remove(id);
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<?> gets(@PathVariable String id){
        return userService.gets(id);
    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        return userService.getAll();
    }
}
