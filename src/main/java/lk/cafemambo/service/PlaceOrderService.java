package lk.cafemambo.service;

import lk.cafemambo.dto.PlaceOrderDto;
import org.springframework.http.ResponseEntity;


public interface PlaceOrderService {

    public ResponseEntity<?> placeOrder(PlaceOrderDto placeOrderDto);

    public ResponseEntity<?> getPendingOrders();

    public ResponseEntity<?> getOrderDetails(String id);

    public ResponseEntity<?> dispatchOrder(String id);

    public ResponseEntity<?> canceledOrder(String id);

    public ResponseEntity<?> allOrderByCustomer(String customerId);
}
