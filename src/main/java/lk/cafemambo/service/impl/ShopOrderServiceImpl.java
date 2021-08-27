package lk.cafemambo.service.impl;

import lk.cafemambo.dto.ShopOrderDto;
import lk.cafemambo.dto.ItemDto;
import lk.cafemambo.dto.PaymentDto;
import lk.cafemambo.dto.ShopTableDto;
import lk.cafemambo.entity.*;
import lk.cafemambo.repository.*;
import lk.cafemambo.security.JwtTokenProvider;
import lk.cafemambo.service.ShopOrderService;
import lk.cafemambo.util.AppConstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ShopOrderServiceImpl implements ShopOrderService {

    @Autowired
    private ShopOrderRepository shopOrderRepository;

    @Autowired
    private ShopOrderDetailsRepository shopOrderDetailsRepository;

    @Autowired
    private ShopTableRepository shopTableRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public ResponseEntity<?> saveShopTable(ShopTableDto shopTableDto) {

        try {


            if(shopTableRepository.findByTableNumber(shopTableDto.getTableNumber()) != null){
                return new ResponseEntity<>("Already Add This Table Number", HttpStatus.INTERNAL_SERVER_ERROR);
            }


            UserEntity userEntity = new UserEntity();
            userEntity.setCreateBy(jwtTokenProvider.getUserEmailByRequestToken());
            userEntity.setCreateDate(new Date());
            userEntity.setEmail(AppConstance.TABLE_EMAIL);
            userEntity.setId(UUID.randomUUID().toString());
            userEntity.setStatus(AppConstance.ACTIVE);
            userEntity.setName(shopTableDto.getTableNumber().toString());
            userEntity.setRole(AppConstance.TABLE_ROLE);

            userRepository.save(userEntity);


            ShopTableEntity shopTableEntity = new ShopTableEntity();
            shopTableEntity.setTableNumber(shopTableDto.getTableNumber());
            shopTableEntity.setCreateDate(new Date());
            shopTableEntity.setId(UUID.randomUUID().toString());
            shopTableEntity.setStatus(AppConstance.ACTIVE);
            shopTableEntity.setToken(jwtTokenProvider.createToken(userEntity.getName()));

            shopTableRepository.save(shopTableEntity);

            return new ResponseEntity<>("200",HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> placeShopOrder(ShopOrderDto shopOrderDto) {

        try {

            UserEntity userEntity = userRepository.findByNameAndRole(shopOrderDto.getTableId().toString(),AppConstance.TABLE_ROLE);

            if(userEntity == null ){
                return new ResponseEntity<>("Invalid User Table",HttpStatus.INTERNAL_SERVER_ERROR);
            }

            ShopTableEntity shopTableEntity = shopTableRepository.findByTableNumber(shopOrderDto.getTableId());

            if(shopTableEntity == null ){
                return new ResponseEntity<>("Invalid Table",HttpStatus.INTERNAL_SERVER_ERROR);
            }

            PaymentEntity paymentEntity = paymentRepository.save(setPaymentEntity(shopOrderDto.getPaymentDto()));

            ShopOrderEntity shopOrderEntity = new ShopOrderEntity();
            shopOrderEntity.setId(UUID.randomUUID().toString());
            shopOrderEntity.setShopTableEntity(shopTableEntity);
            shopOrderEntity.setUserEntity(userEntity);
            shopOrderEntity.setPaymentEntity(paymentEntity);
            shopOrderEntity.setCreateBy(jwtTokenProvider.getUserEmailByRequestToken());
            shopOrderEntity.setLevel(AppConstance.KITCHEN_ORDER_LEVEL);
            shopOrderEntity.setCustomerName(shopOrderDto.getCustomerName());
            shopOrderEntity.setContactNumber(shopOrderDto.getContactNumber());
            shopOrderEntity.setStatus(AppConstance.ACTIVE);

            shopOrderEntity = shopOrderRepository.save(shopOrderEntity);
            Double total = 0.0;
            for (ItemDto itemDto : shopOrderDto.getItemDtoList()) {

                ItemEntity itemEntity = itemRepository.findByIdAndStatus(itemDto.getId(),AppConstance.ACTIVE);

                if(itemEntity != null){

                    ShopOrderDetailsEntity shopOrderDetailsEntity = new ShopOrderDetailsEntity();
                    shopOrderDetailsEntity.setShopOrderEntity(shopOrderEntity);
                    shopOrderDetailsEntity.setCreateBy(jwtTokenProvider.getUserEmailByRequestToken());
                    shopOrderDetailsEntity.setItemEntity(itemEntity);
                    shopOrderDetailsEntity.setCreateDate(new Date());
                    shopOrderDetailsEntity.setId(UUID.randomUUID().toString());
                    shopOrderDetailsEntity.setPrice(itemDto.getPrice());
                    shopOrderDetailsEntity.setQty(itemDto.getQty());
                    shopOrderDetailsEntity.setStatus(AppConstance.ACTIVE);

                    total = new BigDecimal(total).add(new BigDecimal(itemDto.getPrice()).multiply(new BigDecimal(itemDto.getQty()))).setScale(2).doubleValue();

                    shopOrderDetailsRepository.save(shopOrderDetailsEntity);

                }

            }

            shopOrderEntity.setTotal(total);
            shopOrderRepository.save(shopOrderEntity);

            return new ResponseEntity<>("200",HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    private PaymentEntity setPaymentEntity(PaymentDto paymentDto) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setStatus(AppConstance.ACTIVE);
        paymentEntity.setPaymentStatus(paymentDto.getPaymentStatus());
        paymentEntity.setId(UUID.randomUUID().toString());
        paymentEntity.setCreateBy(jwtTokenProvider.getUserEmailByRequestToken());
        paymentEntity.setCreateDate(new Date());
        paymentEntity.setMethod(paymentDto.getMethod());
        paymentEntity.setAmount(paymentDto.getAmount());
        return paymentEntity;

    }

    @Override
    public ResponseEntity<?> getAllShopTable() {

        try {

            List<ShopTableEntity> shopTableEntities = shopTableRepository.findAllByStatus(AppConstance.ACTIVE);

            List<ShopTableDto> shopTableDtoList = new ArrayList<>();
            for (ShopTableEntity shopTableEntity : shopTableEntities) {
                shopTableDtoList.add(setShopTable(shopTableEntity));
            }

            return new ResponseEntity<>(shopTableDtoList,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> updateLevel(String id) {

        try {

            ShopOrderEntity shopOrderEntity = shopOrderRepository.getById(id);

            if(shopOrderEntity == null ){
                return new ResponseEntity<>("Invalid Shop Order",HttpStatus.INTERNAL_SERVER_ERROR);
            }


            if(shopOrderEntity.getLevel().equalsIgnoreCase(AppConstance.KITCHEN_ORDER_LEVEL)){
                shopOrderEntity.setLevel(AppConstance.CASHIER_ORDER_LEVEL);
            }else if (shopOrderEntity.getLevel().equalsIgnoreCase(AppConstance.CASHIER_ORDER_LEVEL)){
                shopOrderEntity.setLevel(AppConstance.COMPLETE_ORDER_LEVEL);
            }

            shopOrderRepository.save(shopOrderEntity);

            return new ResponseEntity<>("200",HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @Override
    public ResponseEntity<?> getAllByUser(String id) {

        try {

            UserEntity userEntity = userRepository.getById(id);

            if(userEntity == null){
                return new ResponseEntity<>("Invalid User Details",HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String level ="";

            if(userEntity.getRole().equalsIgnoreCase(AppConstance.KITCHEN_ROLE)){
                level = AppConstance.KITCHEN_ORDER_LEVEL;
            }else if(userEntity.getRole().equalsIgnoreCase(AppConstance.CASHIER_ROLE)){
                level = AppConstance.CASHIER_ORDER_LEVEL;
            }


            List<ShopOrderEntity> shopOrderEntities = shopOrderRepository.findAllByLevelAndStatus(level,AppConstance.ACTIVE);


            List<ShopOrderDto> shopOrderDtoList = new ArrayList<>();
            for (ShopOrderEntity shopOrderEntity : shopOrderEntities) {
                  shopOrderDtoList.add(SetShopOrderDto(shopOrderEntity));
            }

            return new ResponseEntity<>(shopOrderDtoList,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> getShopOrder(String id) {


        try {

            ShopOrderEntity shopOrderEntity = shopOrderRepository.getById(id);

            if(shopOrderEntity != null){

                ShopOrderDto shopOrderDto = new ShopOrderDto();
                shopOrderDto.setTableId(shopOrderEntity.getShopTableEntity().getTableNumber());
                shopOrderDto.setId(shopOrderEntity.getId());
                shopOrderDto.setContactNumber(shopOrderEntity.getContactNumber());
                shopOrderDto.setCustomerName(shopOrderEntity.getCustomerName());
                shopOrderDto.setPrice(shopOrderEntity.getTotal());

                List<ShopOrderDetailsEntity> shopOrderDetailsEntities = shopOrderDetailsRepository.findAllByStatusAndShopOrderEntity(AppConstance.ACTIVE,shopOrderEntity);


                List<ItemDto> itemDtoList = new ArrayList<>();

                for (ShopOrderDetailsEntity shopOrderDetailsEntity : shopOrderDetailsEntities) {
                    ItemDto itemDto = new ItemDto();
                    itemDto.setCategoryName(shopOrderDetailsEntity.getItemEntity().getCategoryEntity().getName());
                    itemDto.setName(shopOrderDetailsEntity.getItemEntity().getName());
                    itemDto.setPrice(shopOrderDetailsEntity.getPrice());
                    itemDto.setQty(shopOrderDetailsEntity.getQty());
                    itemDto.setId(shopOrderDetailsEntity.getId());
                    itemDto.setImagePath(shopOrderDetailsEntity.getItemEntity().getPath());
                    itemDtoList.add(itemDto);
                }

                shopOrderDto.setItemDtoList(itemDtoList);
                shopOrderDto.setPaymentDto(setPaymentDto(shopOrderEntity.getPaymentEntity()));

                return new ResponseEntity<>(shopOrderDto,HttpStatus.OK);

            }

            return new ResponseEntity<>("Invalid Shop Order",HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private PaymentDto setPaymentDto(PaymentEntity paymentEntity) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setId(paymentEntity.getId());
        paymentDto.setPaymentStatus(paymentEntity.getPaymentStatus());
        paymentDto.setAmount(paymentEntity.getAmount());
        paymentDto.setMethod(paymentEntity.getMethod());
        return paymentDto;
    }

    private ShopOrderDto SetShopOrderDto(ShopOrderEntity shopOrderEntity) {
        ShopOrderDto shopOrderDto = new ShopOrderDto();
        shopOrderDto.setId(shopOrderEntity.getId());
        shopOrderDto.setCustomerName(shopOrderEntity.getCustomerName());
        shopOrderDto.setContactNumber(shopOrderEntity.getContactNumber());
        shopOrderDto.setPrice(shopOrderEntity.getTotal());
        shopOrderDto.setTableId(shopOrderEntity.getShopTableEntity().getTableNumber());
        return shopOrderDto;
    }

    private ShopTableDto setShopTable(ShopTableEntity shopTableEntity) {
        ShopTableDto shopTableDto = new ShopTableDto();
        shopTableDto.setId(shopTableEntity.getId());
        shopTableDto.setTableNumber(shopTableEntity.getTableNumber());
        return shopTableDto;
    }
}
