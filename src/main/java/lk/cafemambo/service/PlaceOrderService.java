package lk.cafemambo.service;

import lk.cafemambo.dto.PlaceOrderDto;
import org.springframework.http.ResponseEntity;

import javax.xml.ws.Response;

public interface PlaceOrderService {

    public ResponseEntity<?> placeOrder(PlaceOrderDto placeOrderDto);

}
