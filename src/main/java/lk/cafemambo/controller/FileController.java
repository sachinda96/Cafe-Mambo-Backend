package lk.cafemambo.controller;

import lk.cafemambo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("file")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/download/{filePath}")
    public ResponseEntity<?> downloadFile(@PathVariable String filePath){
        return fileService.downloadFile(filePath);
    }
}
