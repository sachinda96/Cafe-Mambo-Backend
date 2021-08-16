package lk.cafemambo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.cafemambo.dto.ItemDto;
import lk.cafemambo.dto.PackageDto;
import lk.cafemambo.dto.PackageItemDto;
import lk.cafemambo.entity.ItemEntity;
import lk.cafemambo.entity.PackageDetailsEntity;
import lk.cafemambo.entity.PackageEntity;
import lk.cafemambo.repository.ItemRepository;
import lk.cafemambo.repository.PackageDetailsRepository;
import lk.cafemambo.repository.PackageResposiroty;
import lk.cafemambo.security.JwtTokenProvider;
import lk.cafemambo.service.FileService;
import lk.cafemambo.service.ItemService;
import lk.cafemambo.service.PackageService;
import lk.cafemambo.util.AppConstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PackageServiceImpl implements PackageService {

    @Autowired
    private PackageResposiroty packageResposiroty;

    @Autowired
    private PackageDetailsRepository packageDetailsRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseEntity<?> save(MultipartFile multipartFile, String data) {

        try {

            String packageId= UUID.randomUUID().toString();

            ObjectMapper objectMapper = new ObjectMapper();
            PackageItemDto packageItemDto = objectMapper.readValue(data, PackageItemDto.class);

            if(!multipartFile.isEmpty()){
                if(fileService.uploadFile(packageId,multipartFile)){
                    packageItemDto.setImagePath(packageId+multipartFile.getName());
                }
            }

            PackageEntity packageEntity = packageResposiroty.save(setPackageEntity(packageId,packageItemDto));

            for (String id:packageItemDto.getItemIds()) {
                ItemEntity itemEntity = itemRepository.getById(id);

                if(itemEntity != null){
                    packageDetailsRepository.save(setPackageDetailsEntity(packageEntity,itemEntity));
                }
            }

            return new ResponseEntity<>("200",HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> update(MultipartFile multipartFile, String data) {

        try {


        return new ResponseEntity<>("200",HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getAllPackages() {

        try {

            List<PackageEntity> packageEntities = packageResposiroty.findAllByStatus(AppConstance.ACTIVE);

            List<PackageDto> packageDtos = new ArrayList<>();

            for (PackageEntity packageEntity : packageEntities) {
                packageDtos.add(setPackageDto(packageEntity));
            }

            return new ResponseEntity<>(packageDtos,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    private PackageDetailsEntity setPackageDetailsEntity(PackageEntity packageEntity, ItemEntity itemEntity) {
        PackageDetailsEntity packageDetailsEntity = new PackageDetailsEntity();
        packageDetailsEntity.setPackageEntity(packageEntity);
        packageDetailsEntity.setItemEntity(itemEntity);
        packageDetailsEntity.setCreateBy(jwtTokenProvider.getUserEmailByRequestToken());
        packageDetailsEntity.setStatus(AppConstance.ACTIVE);
        packageDetailsEntity.setId(UUID.randomUUID().toString());
        packageDetailsEntity.setCreateDate(new Date());
        return packageDetailsEntity;
    }

    private PackageEntity setPackageEntity(String packageId, PackageItemDto packageItemDto) {
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setImagePath(packageItemDto.getImagePath());
        packageEntity.setCreateBy(jwtTokenProvider.getUserEmailByRequestToken());
        packageEntity.setDescription(packageItemDto.getDescription());
        packageEntity.setPrice(packageItemDto.getPrice());
        packageEntity.setName(packageItemDto.getName());
        packageEntity.setStatus(AppConstance.ACTIVE);
        packageEntity.setId(packageId);
        packageEntity.setCreateDate(new Date());
        return packageEntity;

    }

    private PackageDto setPackageDto(PackageEntity packageEntity){
        PackageDto packageDto = new PackageDto();
        packageDto.setDescription(packageEntity.getDescription());
        packageDto.setId(packageEntity.getId());
        packageDto.setPrice(packageEntity.getPrice());
        packageDto.setName(packageEntity.getName());
        packageDto.setImagePath(packageEntity.getImagePath());
        return packageDto;
    }
}
