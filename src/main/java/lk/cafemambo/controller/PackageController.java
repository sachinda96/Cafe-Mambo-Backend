package lk.cafemambo.controller;

import lk.cafemambo.service.PackagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/package")
public class PackageController {

    @Autowired
    private PackagesService packagesService;

    @GetMapping
    public ResponseEntity<?> getAllPackages(){
        return packagesService.getAllPackages();
    }
}
