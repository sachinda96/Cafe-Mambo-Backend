package lk.cafemambo.controller;

import lk.cafemambo.service.PackagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value = "/save",produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestParam(name = "file",required = false) MultipartFile file, @RequestParam(name = "data") String data){
        return packagesService.save(file, data);
    }

    @PostMapping(value = "/update",produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestParam(name = "file",required = false) MultipartFile file, @RequestParam(name = "data") String data){
        return packagesService.update(file, data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id){
        return packagesService.get(id);
    }

    @GetMapping("/remove/{id}")
    public ResponseEntity<?> remove(@PathVariable String id){
        return packagesService.remove(id);
    }

}
