package lk.cafemambo.service.impl;

import lk.cafemambo.dto.ItemDto;
import lk.cafemambo.dto.ReviewDto;
import lk.cafemambo.entity.ItemEntity;
import lk.cafemambo.entity.ReviewEntity;
import lk.cafemambo.entity.UserEntity;
import lk.cafemambo.repository.ItemRepository;
import lk.cafemambo.repository.ReviewRepository;
import lk.cafemambo.repository.UserRepository;
import lk.cafemambo.security.JwtTokenProvider;
import lk.cafemambo.service.ItemReviewService;
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
public class ItemReviewServiceImpl implements ItemReviewService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Override
    public ResponseEntity<?> save(ReviewDto reviewDto) {

        try {


            UserEntity userEntity = userRepository.getById(reviewDto.getUserId());

            if(userEntity != null){
                return new ResponseEntity<>("Invalid User Details", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            ItemEntity itemEntity = itemRepository.getById(reviewDto.getItemId());

            if(itemEntity != null){
                return new ResponseEntity<>("Invalid Item Details", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            ReviewEntity reviewEntity = new ReviewEntity();
            reviewEntity.setReview(reviewDto.getReview());
            reviewEntity.setItemEntity(itemEntity);
            reviewEntity.setUserEntity(userEntity);
            reviewEntity.setId(UUID.randomUUID().toString());
            reviewEntity.setLevel(reviewDto.getLevel());
            reviewEntity.setCreateBy(jwtTokenProvider.getUserEmailByRequestToken());
            reviewEntity.setCreateDate(new Date());
            reviewEntity.setStatus(AppConstance.ACTIVE);

            reviewRepository.save(reviewEntity);

            return new ResponseEntity<>("200",HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @Override
    public ResponseEntity<?> getAllByItem(String itemId) {

        try {

            ItemEntity itemEntity = itemRepository.getById(itemId);

            if(itemEntity != null){
                return new ResponseEntity<>("Invalid Item Details", HttpStatus.INTERNAL_SERVER_ERROR);
            }


            List<ReviewEntity> reviewEntities = reviewRepository.findAllByItemEntityAndStatus(itemEntity,AppConstance.ACTIVE);

            List<ReviewDto> reviewDtos = new ArrayList<>();

            for (ReviewEntity reviewEntity : reviewEntities) {
                ReviewDto reviewDto = new ReviewDto();
                reviewDto.setItemId(reviewEntity.getItemEntity().getId());
                reviewDto.setReview(reviewEntity.getReview());
                reviewDto.setRate(reviewEntity.getRate());
                reviewDto.setUserId(reviewEntity.getUserEntity().getId());
                reviewDto.setUserName(reviewEntity.getUserEntity().getName());
                reviewDtos.add(reviewDto);
            }

            return new ResponseEntity<>(reviewDtos,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
