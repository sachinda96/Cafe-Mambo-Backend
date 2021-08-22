package lk.cafemambo.service.impl;

import lk.cafemambo.dto.*;
import lk.cafemambo.entity.*;
import lk.cafemambo.repository.*;
import lk.cafemambo.security.JwtTokenProvider;
import lk.cafemambo.service.PlaceOrderService;
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


            DeliveryDetailsEntity deliveryDetailsEntity = deliveryDetailsRepository.save(setDeliveryDetailsEntity(placeOrderDto.getDeliveryDto()));
            PaymentEntity paymentEntity = paymentRepository.save(setPaymentEntity(placeOrderDto.getPaymentDto()));

            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setOrderDate(placeOrderDto.getOrderDate());
            orderEntity.setId(UUID.randomUUID().toString());
            orderEntity.setCreateBy(jwtTokenProvider.getUserEmailByRequestToken());
            orderEntity.setCreateDate(new Date());
            orderEntity.setUserEntity(userEntity);
            orderEntity.setOrderStatus(AppConstance.ORDER_STATUS_PENDING);
            orderEntity.setStatus(AppConstance.ACTIVE);
            orderEntity.setPaymentEntity(paymentEntity);
            orderEntity.setDeliveryDetailsEntity(deliveryDetailsEntity);
            orderEntity = orderRepository.save(orderEntity);

            Double total = 0.0;


            for (ItemDto itemDto : placeOrderDto.getItemDtoList()) {
                ItemEntity itemEntity = itemRepository.getById(itemDto.getId());
                OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity();
                orderDetailsEntity.setId(UUID.randomUUID().toString());
                orderDetailsEntity.setOrderEntity(orderEntity);
                orderDetailsEntity.setCreateBy(jwtTokenProvider.getUserEmailByRequestToken());
                orderDetailsEntity.setStatus(AppConstance.ACTIVE);
                orderDetailsEntity.setCreateDate(new Date());
                orderDetailsEntity.setItemEntity(itemEntity);
                orderDetailsEntity.setQty(itemDto.getQty());

                total = new BigDecimal(total).add(new BigDecimal(itemEntity.getPrice()).multiply(new BigDecimal(itemDto.getQty()))).setScale(2).doubleValue();

                orderDetailsRepository.save(orderDetailsEntity);

            }

            orderEntity.setTotal(total);
            orderRepository.save(orderEntity);

            return new ResponseEntity<>("200",HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @Override
    public ResponseEntity<?> getPendingOrders() {

        try {

            List<OrderEntity> orderEntities = orderRepository.findAllByOrderStatusAndStatus(AppConstance.ORDER_STATUS_PENDING,AppConstance.ACTIVE);

            List<OrderDto> orderDtoList =  new ArrayList<>();

            for (OrderEntity orderEntity : orderEntities) {
                OrderDto orderDto = new OrderDto();
                orderDto.setId(orderEntity.getId());
                orderDto.setCustomerDto(setCustomerDto(orderEntity.getUserEntity()));
                orderDto.setOrderDate(orderEntity.getOrderDate());
                orderDto.setDeliveryDto(setDeliveryDto(orderEntity.getDeliveryDetailsEntity()));
                orderDto.setPaymentDto(setPaymentDto(orderEntity.getPaymentEntity()));
                orderDto.setItemDtoList(setItemList(orderEntity));
                orderDtoList.add(orderDto);
            }

            return new ResponseEntity<>(orderDtoList,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getOrderDetails(String id) {

        try {

            OrderEntity orderEntity = orderRepository.getById(id);
            OrderDto orderDto = new OrderDto();
            orderDto.setId(orderEntity.getId());
            orderDto.setCustomerDto(setCustomerDto(orderEntity.getUserEntity()));
            orderDto.setOrderDate(orderEntity.getOrderDate());
            orderDto.setDeliveryDto(setDeliveryDto(orderEntity.getDeliveryDetailsEntity()));
            orderDto.setPaymentDto(setPaymentDto(orderEntity.getPaymentEntity()));
            orderDto.setItemDtoList(setItemList(orderEntity));
            orderDto.setTotal(orderEntity.getTotal());

            return new ResponseEntity<>(orderDto,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.OK);
        }

    }

    @Override
    public ResponseEntity<?> dispatchOrder(String id) {

        try {

            OrderEntity orderEntity = orderRepository.getById(id);

            orderEntity.setOrderStatus(AppConstance.ORDER_STATUS_DISPATCHED);

            orderRepository.save(orderEntity);

            return new ResponseEntity<>("200",HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> canceledOrder(String id) {

        try {

            OrderEntity orderEntity = orderRepository.getById(id);

            orderEntity.setOrderStatus(AppConstance.ORDER_STATUS_CANCELED);

            orderRepository.save(orderEntity);

            return new ResponseEntity<>("200",HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> allOrderByCustomer(String customerId) {


        try {

            UserEntity userEntity =userRepository.getById(customerId);

            List<OrderEntity> orderEntities = orderRepository.findAllByUserEntityAndStatus(userEntity,AppConstance.ACTIVE);

            List<OrderDto> orderDtoList = new ArrayList<>();

            for (OrderEntity orderEntity : orderEntities) {
                OrderDto orderDto = new OrderDto();
                orderDto.setId(orderEntity.getId());
                orderDto.setCustomerDto(setCustomerDto(orderEntity.getUserEntity()));
                orderDto.setOrderDate(orderEntity.getOrderDate());
                orderDto.setDeliveryDto(setDeliveryDto(orderEntity.getDeliveryDetailsEntity()));
                orderDto.setPaymentDto(setPaymentDto(orderEntity.getPaymentEntity()));
                orderDto.setItemDtoList(setItemList(orderEntity));
                orderDto.setOrderStatus(orderEntity.getOrderStatus());
                orderDtoList.add(orderDto);
            }

            return new ResponseEntity<>(orderEntities,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private List<ItemDto> setItemList(OrderEntity orderEntity) {

        List<OrderDetailsEntity> orderDetailsEntities = orderDetailsRepository.findAllByStatusAndOrderEntity(AppConstance.ACTIVE,orderEntity);

        List<ItemDto> itemDtoList = new ArrayList<>();

        for (OrderDetailsEntity orderDetailsEntity : orderDetailsEntities) {
            itemDtoList.add(setItemDto(orderDetailsEntity));
        }

        return itemDtoList;
    }

    private ItemDto setItemDto(OrderDetailsEntity orderDetailsEntity){
        ItemDto itemDto = new ItemDto();
        itemDto.setImagePath(orderDetailsEntity.getItemEntity().getPath());
        itemDto.setName(orderDetailsEntity.getItemEntity().getName());
        itemDto.setCategoryName(orderDetailsEntity.getItemEntity().getCategoryEntity().getName());
        itemDto.setQty(orderDetailsEntity.getQty());

        Double price = new BigDecimal(orderDetailsEntity.getItemEntity().getPrice()).multiply(new BigDecimal(orderDetailsEntity.getQty())).setScale(2).doubleValue();
        itemDto.setPrice(price);

        return itemDto;
    }

    private PaymentDto setPaymentDto(PaymentEntity paymentEntity) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPaymentStatus(paymentEntity.getPaymentStatus());
        paymentDto.setMethod(paymentEntity.getMethod());
        paymentDto.setAmount(paymentEntity.getAmount());
        paymentDto.setId(paymentEntity.getId());
        return paymentDto;
    }

    private DeliveryDto setDeliveryDto(DeliveryDetailsEntity deliveryDetailsEntity) {
        DeliveryDto deliveryDto = new DeliveryDto();
        deliveryDto.setDeliveryNote(deliveryDetailsEntity.getDeliveryNote());
        deliveryDto.setDistrict(deliveryDetailsEntity.getDistrict());
        deliveryDto.setCity(deliveryDetailsEntity.getCity());
        deliveryDto.setAddress(deliveryDetailsEntity.getAddress());
        deliveryDto.setId(deliveryDetailsEntity.getId());
        deliveryDto.setMobileNo(deliveryDetailsEntity.getMobileNo());
        return deliveryDto;
    }

    private CustomerDto setCustomerDto(UserEntity userEntity) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setEmail(userEntity.getEmail());
        customerDto.setName(userEntity.getName());
        customerDto.setId(userEntity.getId());
        return customerDto;
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

    private DeliveryDetailsEntity setDeliveryDetailsEntity(DeliveryDto deliveryDto){
       DeliveryDetailsEntity deliveryDetailsEntity = new DeliveryDetailsEntity();
       deliveryDetailsEntity.setStatus(AppConstance.ACTIVE);
       deliveryDetailsEntity.setDeliveryDate(new Date());
       deliveryDetailsEntity.setId(UUID.randomUUID().toString());
       deliveryDetailsEntity.setMobileNo(deliveryDto.getMobileNo());
       deliveryDetailsEntity.setDistrict(deliveryDto.getDistrict());
       deliveryDetailsEntity.setCity(deliveryDto.getCity());
       deliveryDetailsEntity.setAddress(deliveryDto.getAddress());
       deliveryDetailsEntity.setDeliveryNote(deliveryDto.getDeliveryNote());
       deliveryDetailsEntity.setCreateBy(jwtTokenProvider.getUserEmailByRequestToken());
       return deliveryDetailsEntity;
    }
}
