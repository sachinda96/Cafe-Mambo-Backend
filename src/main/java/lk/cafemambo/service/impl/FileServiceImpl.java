package lk.cafemambo.service.impl;

import com.google.cloud.storage.*;
import lk.cafemambo.service.FileService;
import lk.cafemambo.util.CloudConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {


    @Autowired
    private CloudConfig cloudConfig;

    @Value("${bucket}")
    private String bucketName;

    @Override
    public Boolean uploadFile(String id,MultipartFile file) {

        try {

            StorageOptions options = cloudConfig.configStorage();
            Storage storage = options.getService();

            Bucket bucket = storage.get(bucketName);

            bucket.create(id+file.getOriginalFilename(), file.getBytes(), file.getContentType());

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public ResponseEntity<?> downloadFile(String filePath) {

        try {

            StorageOptions options = cloudConfig.configStorage();

            Storage storage = options.getService();

            byte[] read = storage.readAllBytes(BlobId.of(bucketName, filePath));

            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(read));
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Disposition", String.format("attachment; filename=your_file_name"));
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(read.length)
                    .contentType(MediaType.valueOf("application/octet-stream"))
                    .body(resource);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
