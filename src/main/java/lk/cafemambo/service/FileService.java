package lk.cafemambo.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    public Boolean uploadFile(String id,MultipartFile multipartFile);

    public ResponseEntity<?> downloadFile(String filePath);
}
