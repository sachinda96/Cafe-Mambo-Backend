package lk.cafemambo.service.impl;

import lk.cafemambo.dto.ItemDto;
import lk.cafemambo.dto.PaymentDto;
import lk.cafemambo.dto.PlaceOrderDto;
import lk.cafemambo.entity.*;
import lk.cafemambo.repository.*;
import lk.cafemambo.security.JwtTokenProvider;
import lk.cafemambo.service.PlaceOrderService;
import lk.cafemambo.util.AppConstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Service
public class PlaceOrderServiceImpl implements PlaceOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private DeliveryDetailsRepository deliveryDetailsRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<?> placeOrder(PlaceOrderDto placeOrderDto) {

        try {

            UserEntity userEntity = userRepository.getById(placeOrderDto.getUserId());

            if (userEntity == null){
                return new ResponseEntity<>("Invalid User", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setOrderDate(placeOrderDto.getOrderDate());
            orderEntity.setId(UUID.randomUUID().toString());
            orderEntity.setCreateBy(jwtTokenProvider.getUserEmailByRequestToken());
            orderEntity.setCreateDate(new Date());
            orderEntity.setUserEntity(userEntity);
            orderEntity.setStatus(AppConstance.ACTIVE);
            orderEntity.setPaymentEntity(setPaymentEntity(placeOrderDto.getPaymentDto()));

            Double total = 0.0;

            for (ItemDto itemDto : placeOrderDto.getItemDtoList()) {
                ItemEntity itemEntity = itemRepository.getById(itemDto.getId());
                OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity();
                orderDetailsEntity.setOrderEntity(orderEntity);
                orderDetailsEntity.setCreateBy(jwtTokenProvider.getUserEmailByRequestToken());
                orderDetailsEntity.setStatus(AppConstance.ACTIVE);
                orderDetailsEntity.setCreateDate(new Date());
                orderDetailsEntity.setItemEntity(itemEntity);
                orderDetailsEntity.setQty(itemDto.getQty());

                total = new BigDecimal(total).add(new BigDecimal(itemDto.getPrice())).setScale(2).doubleValue();


                orderDetailsRepository.save(orderDetailsEntity);

            }

            orderEntity.setTotal(total);
            orderRepository.save(orderEntity);

            return new ResponseEntity<>("200",HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    private PaymentEntity setPaymentEntity(PaymentDto paymentDto){
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setCreateBy(jwtTokenProvider.getUserEmailByRequestToken());
        paymentEntity.setCreateDate(new Date());
        paymentEntity.setPaymentStatus(paymentDto.getPaymentStatus());
        paymentEntity.setStatus(AppConstance.ACTIVE);
        paymentEntity.setId(UUID.randomUUID().toString());
        paymentEntity.setAmount(paymentDto.getAmount());
        paymentEntity.setMethod(paymentDto.getMethod());
        return paymentEntity;
    }
}
