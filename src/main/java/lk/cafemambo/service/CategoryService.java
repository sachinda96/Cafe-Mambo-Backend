package lk.cafemambo.service;

import lk.cafemambo.dto.CategoryDto;
import lk.cafemambo.entity.CategoryEntity;
import org.springframework.http.ResponseEntity;

public interface CategoryService {

    public ResponseEntity<?> save(CategoryDto categoryDto);

    public ResponseEntity<?> update(CategoryDto categoryDto);

    public ResponseEntity<?> get(String id);

    public ResponseEntity<?> remove(String id);

    public ResponseEntity<?> getAll();

}
