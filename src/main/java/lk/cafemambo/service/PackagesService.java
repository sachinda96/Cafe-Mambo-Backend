package lk.cafemambo.service;

import lk.cafemambo.dto.PackageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface PackagesService {

    public ResponseEntity<?> getAllPackages();

    public ResponseEntity<?> save(MultipartFile multipartFile, String data);

    public ResponseEntity<?> update(MultipartFile multipartFile, String data);

    public ResponseEntity<?> remove(String packageId);

    public ResponseEntity<?> get(String id);


}
