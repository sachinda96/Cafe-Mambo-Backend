package lk.cafemambo.controller;

import lk.cafemambo.dto.CategoryDto;
import lk.cafemambo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public ResponseEntity<?> save(@RequestBody CategoryDto categoryDto){
        return categoryService.save(categoryDto);
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody CategoryDto categoryDto){
        return categoryService.update(categoryDto);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> get(@PathVariable String id){
        return categoryService.get(id);
    }

    @GetMapping("/remove/{id}")
    public ResponseEntity<?> remove(@PathVariable String id){
        return categoryService.remove(id);
    }

    @GetMapping("/getall")
    public ResponseEntity<?> getAll(){
        return categoryService.getAll();
    }
}
