package lk.cafemambo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lk.cafemambo.dto.ItemDto;
import lk.cafemambo.service.ItemService;
import lk.cafemambo.util.CloudConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping(value = "/save",produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveItem(@RequestParam(name = "file",required = false) MultipartFile file,@RequestParam(name = "data") String data) {
        return itemService.saveItem(file,data);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<?> updateItem(@RequestParam(name = "file",required = false) MultipartFile imageFile,@RequestParam(name = "data") String data){
        return itemService.updateItem(imageFile,data);
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<?> getAllItem(){
        return itemService.getAllItem();
    }

    @GetMapping(value = "/getItem/{id}")
    public ResponseEntity<?> getItem(@PathVariable String id){
        return itemService.getItem(id);
    }

    @GetMapping(value = "/removeItem/{id}")
    public ResponseEntity<?> removeItem(@PathVariable String id){
        return itemService.removeItem(id);
    }

    @GetMapping(value = "/getAllItemByCategory/{categoryId}")
    public ResponseEntity<?> getAllItemByCategory(@PathVariable String categoryId){
        return itemService.getAllItemByCategory(categoryId);
    }

    @GetMapping(value = "/getAllItemsByIndexAndCategory/{index}/{size}/{categoryId}")
    public ResponseEntity<?> getAllItemByIndexAndCategory(@PathVariable Integer index,@PathVariable Integer size,@PathVariable String categoryId){
        return itemService.getAllItemByIndexAndCategory(index,size,categoryId);
    }

    @GetMapping(value = "/getAllItemByIndex/{index}/{size}")
    public ResponseEntity<?> getAllItemByIndex(Integer index,Integer size){
        return itemService.getAllItemByIndex(index,size);
    }


    @GetMapping(value = "/itemCountByCategory/{categoryid}")
    public ResponseEntity<?> getAllItemByIndex(@PathVariable String categoryid){
        return itemService.getItemCountByCategory(categoryid);
    }

}
