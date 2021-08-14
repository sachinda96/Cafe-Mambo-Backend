package lk.cafemambo.service.impl;

import lk.cafemambo.dto.CategoryDto;
import lk.cafemambo.entity.CategoryEntity;
import lk.cafemambo.repository.CategoryRepository;
import lk.cafemambo.security.JwtTokenProvider;
import lk.cafemambo.service.CategoryService;
import lk.cafemambo.util.AppConstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseEntity<?> save(CategoryDto categoryDto) {

        try {

            categoryRepository.save(setCategoryEntity(categoryDto));

            return new ResponseEntity<>("200",HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> update(CategoryDto categoryDto) {
        try {

            CategoryEntity categoryEntity = categoryRepository.getById(categoryDto.getId());

            if (categoryEntity == null){
                return new ResponseEntity<>("Invalid Category",HttpStatus.INTERNAL_SERVER_ERROR);
            }

            categoryRepository.save(updateCategoryStatus(AppConstance.INACTIVE,categoryEntity));

            categoryRepository.save(setCategoryEntity(categoryDto));

            return new ResponseEntity<>("200",HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> get(String id) {

        try {

            CategoryEntity categoryEntity = categoryRepository.getById(id);

            if (categoryEntity == null){
                return new ResponseEntity<>("Invalid Category",HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(setCategoryDto(categoryEntity),HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> remove(String id) {

        try {

            CategoryEntity categoryEntity = categoryRepository.getById(id);

            if (categoryEntity == null){
                return new ResponseEntity<>("Invalid Category",HttpStatus.INTERNAL_SERVER_ERROR);
            }

            categoryRepository.save(updateCategoryStatus(AppConstance.ACTIVE,categoryEntity));

            return new ResponseEntity<>("200",HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getAll() {

        try {

            List<CategoryEntity> categoryEntities = categoryRepository.findAllByStatus(AppConstance.ACTIVE);

            List<CategoryDto> categoryDtoList = new ArrayList<>();

            for (CategoryEntity categoryEntity :  categoryEntities) {
                categoryDtoList.add(setCategoryDto(categoryEntity));
            }

            return new ResponseEntity<>(categoryDtoList,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.OK);
        }
    }

    public CategoryEntity setCategoryEntity(CategoryDto categoryDto){
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCreateBy(jwtTokenProvider.getUserEmailByRequestToken());
        categoryEntity.setId(UUID.randomUUID().toString());
        categoryEntity.setName(categoryDto.getName());
        categoryEntity.setStatus(AppConstance.ACTIVE);
        categoryEntity.setCreateDate(new Date());
        return categoryEntity;
    }

    public CategoryDto setCategoryDto(CategoryEntity categoryEntity){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(categoryEntity.getId());
        categoryDto.setName(categoryEntity.getName());
        return categoryDto;
    }

    public CategoryEntity updateCategoryStatus(String status,CategoryEntity categoryEntity){
        categoryEntity.setStatus(status);
        categoryEntity.setUpdateBy(jwtTokenProvider.getUserEmailByRequestToken());
        categoryEntity.setUpdateDate(new Date());
        return categoryEntity;
    }


}
