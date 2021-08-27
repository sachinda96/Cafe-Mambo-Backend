package lk.cafemambo.service.impl;

import lk.cafemambo.dto.DeliveryDto;
import lk.cafemambo.dto.EventBookingDto;
import lk.cafemambo.dto.PaymentDto;
import lk.cafemambo.entity.*;
import lk.cafemambo.repository.*;
import lk.cafemambo.security.JwtTokenProvider;
import lk.cafemambo.service.EventBookingService;
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
public class EventBookingServiceImpl implements EventBookingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventBookingRepository eventBookingRepository;

    @Autowired
    private EventBookingDetailsRepository eventBookingDetailsRepository;

    @Autowired
    private DeliveryDetailsRepository deliveryDetailsRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PackageResposiroty packageResposiroty;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseEntity<?> addBooking(EventBookingDto eventBookingDto) {

        try {

            PackageEntity packageEntity = packageResposiroty.getById(eventBookingDto.getPackageId());

            if (packageEntity == null){
                return new ResponseEntity<>("Invalid Package", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            EventBookingEntity eventBookingEntity = eventBookingRepository.save(setEventBookingEntity(eventBookingDto));

            eventBookingDetailsRepository.save(setEventBookingDetailsEntity(eventBookingEntity,packageEntity));

            return new ResponseEntity<>("200", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @Override
    public ResponseEntity<?> updateBooking(EventBookingDto eventBookingDto) {

        try {

            if(removeBooking(eventBookingDto.getId()).getStatusCode().equals(HttpStatus.OK)){
                if(addBooking(eventBookingDto).getStatusCode().equals(HttpStatus.OK)){
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
    public ResponseEntity<?> removeBooking(String id) {

        try {

            EventBookingEntity eventBookingEntity = eventBookingRepository.getById(id);

            if(eventBookingEntity == null){
                return new ResponseEntity<>("Invalid Event Booking", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            eventBookingEntity.setStatus(AppConstance.INACTIVE);
            eventBookingEntity.setUpdateBy(jwtTokenProvider.getUserEmailByRequestToken());
            eventBookingEntity.setUpdateDate(new Date());
            eventBookingRepository.save(eventBookingEntity);


            EventBookingDetailsEntity eventBookingDetailsEntity = eventBookingDetailsRepository.findByEventBookingEntityAndStatus(eventBookingEntity,AppConstance.ACTIVE);

            if(eventBookingDetailsEntity != null){
                eventBookingDetailsEntity.setStatus(AppConstance.INACTIVE);
                eventBookingDetailsEntity.setUpdateBy(jwtTokenProvider.getUserEmailByRequestToken());
                eventBookingDetailsEntity.setUpdateDate(new Date());
                eventBookingDetailsRepository.save(eventBookingDetailsEntity);
            }

            PaymentEntity paymentEntity = eventBookingEntity.getPaymentEntity();

            if(paymentEntity != null){
                paymentEntity.setStatus(AppConstance.INACTIVE);
                paymentEntity.setUpdateBy(jwtTokenProvider.getUserEmailByRequestToken());
                paymentEntity.setUpdateDate(new Date());
            }

            DeliveryDetailsEntity deliveryDetailsEntity= eventBookingEntity.getDeliveryDetailsEntity();

            if(deliveryDetailsEntity != null){
                deliveryDetailsEntity.setStatus(AppConstance.INACTIVE);
                deliveryDetailsEntity.setUpdateBy(jwtTokenProvider.getUserEmailByRequestToken());
                deliveryDetailsEntity.setUpdateDate(new Date());
            }

            return new ResponseEntity<>("200",HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> getBooking(String id) {

        try {

            EventBookingEntity eventBookingEntity = eventBookingRepository.getById(id);

            if(eventBookingEntity == null){
                return new ResponseEntity<>("Invalid Event Booking", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(setEventBookingDto(eventBookingEntity),HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getAllBooking() {
        try {

            List<EventBookingEntity> eventBookingEntityList = eventBookingRepository.findAllByStatus(AppConstance.ACTIVE);

            List<EventBookingDto> eventBookingDtoList = new ArrayList<>();

            for (EventBookingEntity eventBookingEntity : eventBookingEntityList) {
                eventBookingDtoList.add(setEventBookingDto(eventBookingEntity));
            }

            return new ResponseEntity<>(eventBookingDtoList,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getAllPendingBooking() {

        try {

            List<EventBookingEntity> eventBookingEntityList = eventBookingRepository.findAllByStatusAndEventStatus(AppConstance.ACTIVE,AppConstance.EVENT_STATUS_PENDING);

            List<EventBookingDto> eventBookingDtoList = new ArrayList<>();

            for (EventBookingEntity eventBookingEntity : eventBookingEntityList) {
                eventBookingDtoList.add(setEventBookingDto(eventBookingEntity));
            }

            return new ResponseEntity<>(eventBookingDtoList,HttpStatus.OK);


        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @Override
    public ResponseEntity<?> completeEvent(String id) {

        try {

            EventBookingEntity eventBookingEntity = eventBookingRepository.getById(id);

            if(eventBookingEntity == null){
                return new ResponseEntity<>("Invalid Event",HttpStatus.INTERNAL_SERVER_ERROR);
            }

            eventBookingEntity.setEventStatus(AppConstance.EVENT_STATUS_COMPLETE);
            eventBookingRepository.save(eventBookingEntity);

            return new ResponseEntity<>("200",HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> getAllEventBookingsByCustomer(String id) {

        try {

            List<EventBookingEntity> eventBookingEntityList =  eventBookingRepository.findAllByUserIdsAndStatus(id,AppConstance.ACTIVE);

            List<EventBookingDto> eventBookingDtoList = new ArrayList<>();

            for (EventBookingEntity eventBookingEntity : eventBookingEntityList) {
                EventBookingDto eventBookingDto = new EventBookingDto();
                eventBookingDto.setContactNumber(eventBookingEntity.getContactNumber());
                eventBookingDto.setMessage(eventBookingEntity.getMessage());
                eventBookingDto.setLocation(eventBookingEntity.getLocation());
                eventBookingDto.setName(eventBookingEntity.getName());
                eventBookingDto.setEmail(eventBookingEntity.getEmail());

                EventBookingDetailsEntity eventBookingDetailsEntity = eventBookingDetailsRepository.findByEventBookingEntityAndStatus(eventBookingEntity,AppConstance.ACTIVE);

                if(eventBookingDetailsEntity != null){
                    eventBookingDto.setPackageName(eventBookingDetailsEntity.getPackageEntity().getName());
                }

                eventBookingDto.setBookDate(eventBookingEntity.getBookDate());

                eventBookingDtoList.add(eventBookingDto);
            }

            return new ResponseEntity<>(eventBookingDtoList,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private EventBookingEntity setEventBookingEntity(EventBookingDto eventBookingDto){

        EventBookingEntity eventBookingEntity = new EventBookingEntity();
        eventBookingEntity.setCreateBy(jwtTokenProvider.getUserEmailByRequestToken());
        eventBookingEntity.setBookDate(eventBookingDto.getBookDate());
        eventBookingEntity.setCreateDate(new Date());
        eventBookingEntity.setId(UUID.randomUUID().toString());
        eventBookingEntity.setStatus(AppConstance.ACTIVE);
        eventBookingEntity.setEventStatus(AppConstance.EVENT_STATUS_PENDING);
        eventBookingEntity.setLocation(eventBookingDto.getLocation());
        eventBookingEntity.setMessage(eventBookingDto.getMessage());
        eventBookingEntity.setContactNumber(eventBookingDto.getContactNumber());
        eventBookingEntity.setEmail(eventBookingDto.getEmail());
        eventBookingEntity.setName(eventBookingDto.getName());
        eventBookingEntity.setUserId(eventBookingDto.getUserId());

        return eventBookingEntity;
    }


    private EventBookingDetailsEntity setEventBookingDetailsEntity(EventBookingEntity eventBookingEntity, PackageEntity packageEntity){

        EventBookingDetailsEntity eventBookingDetailsEntity = new EventBookingDetailsEntity();
        eventBookingDetailsEntity.setEventBookingEntity(eventBookingEntity);
        eventBookingDetailsEntity.setPackageEntity(packageEntity);
        eventBookingDetailsEntity.setCreateBy(jwtTokenProvider.getUserEmailByRequestToken());
        eventBookingDetailsEntity.setStatus(AppConstance.ACTIVE);
        eventBookingDetailsEntity.setId(UUID.randomUUID().toString());
        eventBookingDetailsEntity.setCreateDate(new Date());
        return eventBookingDetailsEntity;
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
        deliveryDetailsEntity.setDeliveryDate(deliveryDto.getDeliveryDate());
        deliveryDetailsEntity.setDeliveryNote(deliveryDto.getDeliveryNote());
        deliveryDetailsEntity.setAddress(deliveryDto.getAddress());
        deliveryDetailsEntity.setName(deliveryDto.getName());
        deliveryDetailsEntity.setCreateBy(jwtTokenProvider.getUserEmailByRequestToken());
        deliveryDetailsEntity.setCreateDate(new Date());
        deliveryDetailsEntity.setId(UUID.randomUUID().toString());
        deliveryDetailsEntity.setCity(deliveryDto.getCity());
        deliveryDetailsEntity.setDistrict(deliveryDto.getDistrict());
        deliveryDetailsEntity.setMobileNo(deliveryDto.getMobileNo());
        deliveryDetailsEntity.setPostalCode(deliveryDto.getPostalCode());
        return deliveryDetailsEntity;
    }

    public EventBookingDto setEventBookingDto(EventBookingEntity eventBookingEntity){
        EventBookingDto eventBookingDto = new EventBookingDto();
        eventBookingDto.setBookDate(eventBookingEntity.getBookDate());
        eventBookingDto.setId(eventBookingEntity.getId());
        eventBookingDto.setEmail(eventBookingEntity.getEmail());
        eventBookingDto.setName(eventBookingEntity.getEmail());
        eventBookingDto.setLocation(eventBookingEntity.getLocation());
        eventBookingDto.setMessage(eventBookingEntity.getMessage());
        eventBookingDto.setContactNumber(eventBookingEntity.getContactNumber());
        EventBookingDetailsEntity eventBookingDetailsEntity = eventBookingDetailsRepository.findByEventBookingEntityAndStatus(eventBookingEntity,AppConstance.ACTIVE);
        if(eventBookingDetailsEntity != null){
            eventBookingDto.setPackageId(eventBookingDetailsEntity.getPackageEntity().getId());
            eventBookingDto.setPackageName(eventBookingDetailsEntity.getPackageEntity().getName());
        }
        return eventBookingDto;
    }

    private PaymentDto setPaymentDto(PaymentEntity paymentEntity) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPaymentStatus(paymentEntity.getPaymentStatus());
        paymentDto.setId(paymentEntity.getId());
        paymentDto.setAmount(paymentEntity.getAmount());
        paymentDto.setMethod(paymentEntity.getMethod());
        return paymentDto;
    }

    private DeliveryDto setDeliveryDto(DeliveryDetailsEntity deliveryDetailsEntity) {
        DeliveryDto deliveryDto = new DeliveryDto();
        deliveryDto.setDeliveryDate(deliveryDetailsEntity.getDeliveryDate());
        deliveryDto.setDeliveryNote(deliveryDetailsEntity.getDeliveryNote());
        deliveryDto.setId(deliveryDetailsEntity.getId());
        deliveryDto.setName(deliveryDetailsEntity.getName());
        deliveryDto.setCity(deliveryDetailsEntity.getCity());
        deliveryDto.setAddress(deliveryDetailsEntity.getAddress());
        deliveryDto.setCity(deliveryDetailsEntity.getCity());
        deliveryDto.setDistrict(deliveryDetailsEntity.getDistrict());
        deliveryDto.setMobileNo(deliveryDetailsEntity.getMobileNo());
        deliveryDto.setPostalCode(deliveryDetailsEntity.getPostalCode());
        return deliveryDto;
    }

}
