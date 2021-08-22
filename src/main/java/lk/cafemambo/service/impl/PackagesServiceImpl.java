package lk.cafemambo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.cafemambo.dto.ItemDto;
import lk.cafemambo.dto.PackageDto;
import lk.cafemambo.entity.ItemEntity;
import lk.cafemambo.entity.PackageDetailsEntity;
import lk.cafemambo.entity.PackageEntity;
import lk.cafemambo.repository.ItemRepository;
import lk.cafemambo.repository.PackageDetailsRepository;
import lk.cafemambo.repository.PackageResposiroty;
import lk.cafemambo.security.JwtTokenProvider;
import lk.cafemambo.service.FileService;
import lk.cafemambo.service.PackagesService;
import lk.cafemambo.util.AppConstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PackagesServiceImpl implements PackagesService{

    @Autowired
    private PackageDetailsRepository packageDetailsRepository;

    @Autowired
    private PackageResposiroty packageResposiroty;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private FileService fileService;

    @Value("${filepath}")
    private String filePath;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseEntity<?> getAllPackages() {

        try {

            List<PackageEntity> packageEntities = packageResposiroty.findAllByStatus(AppConstance.ACTIVE);

            List<PackageDto> packageDtoList = new ArrayList<>();

            for (PackageEntity packageEntity : packageEntities) {
                packageDtoList.add(setPackage(packageEntity));
            }

            return new ResponseEntity<>(packageDtoList,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @Override
    public ResponseEntity<?> save(MultipartFile multipartFile, String data) {

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            PackageDto packageDto = objectMapper.readValue(data, PackageDto.class);

            String id= UUID.randomUUID().toString();
            if(multipartFile != null){
                if(fileService.uploadFile(id,multipartFile)){
                    packageDto.setImagePath(generateImagePath(id,multipartFile.getOriginalFilename()));
                }
            }

            PackageEntity packageEntity = packageResposiroty.save(setPackageEntity(id,packageDto));

            for (String itemId : packageDto.getItemIdList()) {
                ItemEntity itemEntity = itemRepository.getById(itemId);
                if(itemEntity != null){
                    packageDetailsRepository.save(setPackageDetailsEntity(packageEntity,itemEntity));
                }
            }

            return new ResponseEntity<>("200",HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private PackageDetailsEntity setPackageDetailsEntity(PackageEntity packageEntity, ItemEntity itemEntity) {

        PackageDetailsEntity packageDetailsEntity = new PackageDetailsEntity();
        packageDetailsEntity.setItemEntity(itemEntity);
        packageDetailsEntity.setPackageEntity(packageEntity);
        packageDetailsEntity.setId(UUID.randomUUID().toString());
        packageDetailsEntity.setStatus(AppConstance.ACTIVE);
        packageDetailsEntity.setCreateDate(new Date());
        packageDetailsEntity.setCreateBy(jwtTokenProvider.getUserEmailByRequestToken());
        return packageDetailsEntity;
    }

    private PackageEntity setPackageEntity(String id,PackageDto packageDto) {
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setCreateDate(new Date());
        packageEntity.setId(id);
        packageEntity.setStatus(AppConstance.ACTIVE);
        packageEntity.setName(packageDto.getName());
        packageEntity.setPrice(packageDto.getPrice());
        packageEntity.setDescription(packageDto.getDescription());
        packageEntity.setCreateBy(jwtTokenProvider.getUserEmailByRequestToken());
        packageEntity.setImagePath(packageDto.getImagePath());
        return packageEntity;
    }

    @Override
    public ResponseEntity<?> update(MultipartFile multipartFile, String data) {

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            PackageDto packageDto = objectMapper.readValue(data, PackageDto.class);

            if(remove(packageDto.getId()).getStatusCode().equals(HttpStatus.OK)){
                if(save(multipartFile, data).getStatusCode().equals(HttpStatus.OK)){
                    return new ResponseEntity<>("200",HttpStatus.OK);
                }
            }

            return new ResponseEntity<>("Failed to update",HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> remove(String packageId) {

        try {

            PackageEntity packageEntity = packageResposiroty.getById(packageId);

            if(packageEntity == null){
                return new ResponseEntity<>("Invalid Package Details",HttpStatus.INTERNAL_SERVER_ERROR);
            }

            List<PackageDetailsEntity> packageDetailsEntities = packageDetailsRepository.findAllByPackageEntityAndStatus(packageEntity,AppConstance.ACTIVE);

            for (PackageDetailsEntity packageDetailsEntity : packageDetailsEntities) {
                packageDetailsEntity.setStatus(AppConstance.INACTIVE);
                packageDetailsRepository.save(packageDetailsEntity);
            }

            packageEntity.setStatus(AppConstance.INACTIVE);

            packageResposiroty.save(packageEntity);

            return new ResponseEntity<>("200",HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> get(String id) {

        try {

            PackageEntity packageEntity = packageResposiroty.getById(id);

            if(packageEntity == null){
                return new ResponseEntity<>("Invalid Package",HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(setPackageItem(packageEntity),HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private PackageDto setPackage(PackageEntity packageEntity) {
        PackageDto packageDto = new PackageDto();
        packageDto.setId(packageEntity.getId());
        packageDto.setImagePath(packageEntity.getImagePath());
        packageDto.setName(packageEntity.getName());
        packageDto.setPrice(packageEntity.getPrice());
        packageDto.setDescription(packageEntity.getDescription());
        return packageDto;
    }

    private PackageDto setPackageItem(PackageEntity packageEntity) {
        PackageDto packageDto = new PackageDto();
        packageDto.setId(packageEntity.getId());
        packageDto.setImagePath(packageEntity.getImagePath());
        packageDto.setName(packageEntity.getName());
        packageDto.setPrice(packageEntity.getPrice());
        packageDto.setDescription(packageEntity.getDescription());

        List<PackageDetailsEntity> packageDetailsEntities =  packageDetailsRepository.findAllByPackageEntityAndStatus(packageEntity,AppConstance.ACTIVE);

        List<String> itemIdList = new ArrayList<>();

        for (PackageDetailsEntity packageDetailsEntity : packageDetailsEntities) {
            itemIdList.add(packageDetailsEntity.getItemEntity().getId());
        }

        packageDto.setItemIdList(itemIdList);
        return packageDto;
    }

    private String generateImagePath(String id,String fileName) {
        return filePath+id+""+fileName+"";
    }
}
