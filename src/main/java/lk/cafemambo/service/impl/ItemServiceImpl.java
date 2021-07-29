package lk.cafemambo.service.impl;

import lk.cafemambo.dto.ItemDto;
import lk.cafemambo.entity.CategoryEntity;
import lk.cafemambo.entity.ItemEntity;
import lk.cafemambo.repository.CategoryRepository;
import lk.cafemambo.repository.ItemRepository;
import lk.cafemambo.security.JwtTokenProvider;
import lk.cafemambo.service.ItemService;
import lk.cafemambo.util.AppConstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;



    @Override
    public ResponseEntity<?> saveItem(MultipartFile imageFile, ItemDto itemDto) {

        try {

            CategoryEntity  categoryEntity = categoryRepository.getById(itemDto.getCategoryId());

            if(categoryEntity == null){
                return new ResponseEntity<>("Invalid Category", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            itemRepository.save(setItemEntity(UUID.randomUUID().toString(),itemDto,categoryEntity));

            return new ResponseEntity<>("200",HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> updateItem(MultipartFile imageFile, ItemDto itemDto) {
        try {

            CategoryEntity  categoryEntity = categoryRepository.getById(itemDto.getCategoryId());

            if(categoryEntity == null){
                return new ResponseEntity<>("Invalid Category", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (removeItem(itemDto.getId()).getStatusCode().equals(HttpStatus.OK)){
                itemRepository.save(setItemEntity(UUID.randomUUID().toString(),itemDto,categoryEntity));

                return new ResponseEntity<>("200",HttpStatus.OK);
            }

            return new ResponseEntity<>("Failed to update",HttpStatus.INTERNAL_SERVER_ERROR);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getAllItem() {
        try {

            List<ItemEntity> itemEntityList = itemRepository.findAllByStatus(AppConstance.ACTIVE);

            List<ItemDto> itemDtoList = new ArrayList<>();

            for (ItemEntity itemEntity:itemEntityList) {
                itemDtoList.add(setItemDto(itemEntity));
            }

            return new ResponseEntity<>(itemDtoList,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> getItem(String id) {
        try {

            ItemEntity itemEntity = itemRepository.findByIdAndStatus(id,AppConstance.ACTIVE);

            if(itemEntity == null){
                return new ResponseEntity<>("Invalid Item",HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(setItemDto(itemEntity),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> removeItem(String id) {

        try {

            ItemEntity itemEntity = itemRepository.getById(id);

            if(itemEntity != null){
                itemEntity.setStatus(AppConstance.INACTIVE);
                itemRepository.save(itemEntity);
            }

            return new ResponseEntity<>("200",HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getAllItemByCategory(String categoryId) {

        try {

            CategoryEntity  categoryEntity = categoryRepository.getById(categoryId);

            if(categoryEntity == null){
                return new ResponseEntity<>("Invalid Category", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            List<ItemEntity> itemEntityList = itemRepository.findAllByCategoryEntityAndStatus(categoryEntity,AppConstance.ACTIVE);

            List<ItemDto> itemDtoList = new ArrayList<>();

            for (ItemEntity itemEntity:itemEntityList) {
                itemDtoList.add(setItemDto(itemEntity));
            }

            return new ResponseEntity<>(itemDtoList,HttpStatus.OK);
        }catch (Exception e){
           e.printStackTrace();
           return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @Override
    public ResponseEntity<?> getAllItemByIndexAndCategory(Integer index, Integer size, String categoryId) {
        try {

            Pageable paging = PageRequest.of(index, size);

            CategoryEntity  categoryEntity = categoryRepository.getById(categoryId);

            if(categoryEntity == null){
                return new ResponseEntity<>("Invalid Category", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            List<ItemEntity> itemEntityList = itemRepository.findAllByCategoryEntityAndStatus(categoryEntity,AppConstance.ACTIVE,paging);

            List<ItemDto> itemDtoList = new ArrayList<>();

            for (ItemEntity itemEntity:itemEntityList) {
                itemDtoList.add(setItemDto(itemEntity));
            }

            return new ResponseEntity<>(itemDtoList,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getAllItemByIndex(Integer index, Integer size) {
        try {

            Pageable paging = PageRequest.of(index, size);
            List<ItemEntity> itemEntityList = itemRepository.findAllByStatus(AppConstance.ACTIVE,paging);

            List<ItemDto> itemDtoList = new ArrayList<>();

            for (ItemEntity itemEntity:itemEntityList) {
                itemDtoList.add(setItemDto(itemEntity));
            }

            return new ResponseEntity<>(itemDtoList,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ItemEntity setItemEntity(String id,ItemDto itemDto,CategoryEntity categoryEntity)throws Exception{
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setCategoryEntity(categoryEntity);
        itemEntity.setPath(itemDto.getImagePath());
        itemEntity.setCreateBy(getUserEmail());
        itemEntity.setCreateDate(new Date());
        itemEntity.setId(id);
        itemEntity.setStatus(AppConstance.ACTIVE);
        itemEntity.setPrice(itemDto.getPrice());
        itemEntity.setName(itemDto.getName());
        itemEntity.setDescription(itemDto.getDescription());
        itemEntity.setIngredients(itemDto.getIngredients());
        return itemEntity;
    }

    private ItemDto setItemDto(ItemEntity itemEntity)throws Exception{
        ItemDto itemDto = new ItemDto();
        itemDto.setCategoryId(itemEntity.getCategoryEntity().getId());
        itemDto.setImagePath(itemEntity.getPath());
        itemDto.setDescription(itemEntity.getDescription());
        itemDto.setIngredients(itemEntity.getIngredients());
        itemDto.setId(itemEntity.getId());
        itemDto.setName(itemEntity.getName());
        itemDto.setPrice(itemEntity.getPrice());
        itemDto.setRateCount(generateRate());
        return itemDto;
    }

    private Integer generateRate() {
        return 0;
    }

    private String getUserEmail() {
        return jwtTokenProvider.getUsernameFromToken(jwtTokenProvider.resolveToken(request));
    }
}
