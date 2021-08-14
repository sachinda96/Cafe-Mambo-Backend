package lk.cafemambo.controller;

import lk.cafemambo.dto.PlaceOrderDto;
import lk.cafemambo.service.PlaceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private PlaceOrderService placeOrderService;

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody PlaceOrderDto placeOrderDto){
        return placeOrderService.placeOrder(placeOrderDto);
    }

}
