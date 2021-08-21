package lk.cafemambo.service.impl;

import lk.cafemambo.dto.PackageDto;
import lk.cafemambo.entity.PackageEntity;
import lk.cafemambo.repository.PackageDetailsRepository;
import lk.cafemambo.repository.PackageResposiroty;
import lk.cafemambo.service.PackagesService;
import lk.cafemambo.util.AppConstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PackagesServiceImpl implements PackagesService{

    @Autowired
    private PackageDetailsRepository packageDetailsRepository;

    @Autowired
    private PackageResposiroty packageResposiroty;


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

    private PackageDto setPackage(PackageEntity packageEntity) {
        PackageDto packageDto = new PackageDto();
        packageDto.setImagePath(packageEntity.getImagePath());
        packageDto.setName(packageEntity.getName());
        packageDto.setPrice(packageEntity.getPrice());
        packageDto.setDescription(packageEntity.getDescription());
        return packageDto;
    }
}
