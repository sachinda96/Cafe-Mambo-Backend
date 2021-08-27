package lk.cafemambo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class Test {

    @GetMapping("/test")
    public ResponseEntity<?> get() throws InterruptedException {
        Thread.sleep(200000);
        return new ResponseEntity<>("200", HttpStatus.OK);
    }
}
