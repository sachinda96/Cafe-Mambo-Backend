package lk.cafemambo.controller;

import lk.cafemambo.dto.ItemDto;
import lk.cafemambo.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<?> saveItem(@RequestParam(name = "file",required = false) MultipartFile imageFile,@RequestBody ItemDto itemDto){
        return itemService.saveItem(imageFile,itemDto);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<?> updateItem(@RequestParam(name = "file",required = false) MultipartFile imageFile,@RequestBody ItemDto itemDto){
        return itemService.updateItem(imageFile,itemDto);
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
    public ResponseEntity<?> removeItem(String id){
        return itemService.removeItem(id);
    }

    @GetMapping(value = "/getAllItemByCategory/{categoryId}")
    public ResponseEntity<?> getAllItemByCategory(@PathVariable String categoryId){
        return itemService.getAllItemByCategory(categoryId);
    }

    @GetMapping(value = "/getAllItemByIndexAndCategory/{index}/{size}/{categoryId}")
    public ResponseEntity<?> getAllItemByIndexAndCategory(@PathVariable Integer index,@PathVariable Integer size,@PathVariable String categoryId){
        return itemService.getAllItemByIndexAndCategory(index,size,categoryId);
    }

    @GetMapping(value = "/getAllItemByIndex/{index}/{size}")
    public ResponseEntity<?> getAllItemByIndex(Integer index,Integer size){
        return itemService.getAllItemByIndex(index,size);
    }
}
