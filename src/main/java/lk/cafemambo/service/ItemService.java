package lk.cafemambo.service;

import lk.cafemambo.dto.ItemDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ItemService {

    public ResponseEntity<?> saveItem(MultipartFile imageFile, ItemDto itemDto);

    public ResponseEntity<?> updateItem(MultipartFile imageFile, ItemDto itemDto);

    public ResponseEntity<?> getAllItem();

    public ResponseEntity<?> getItem(String id);

    public ResponseEntity<?> removeItem(String id);

    public ResponseEntity<?> getAllItemByCategory(String categoryId);

    public ResponseEntity<?> getAllItemByIndexAndCategory(Integer index,Integer size,String categoryId);

    public ResponseEntity<?> getAllItemByIndex(Integer index,Integer size);
}
