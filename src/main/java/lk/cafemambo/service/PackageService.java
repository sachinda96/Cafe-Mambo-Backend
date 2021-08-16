package lk.cafemambo.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface PackageService {

    public ResponseEntity<?> save(MultipartFile multipartFile,String data);

    public ResponseEntity<?> update(MultipartFile multipartFile,String data);

    public ResponseEntity<?> getAllPackages();
}
