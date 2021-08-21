package lk.cafemambo.controller;

import lk.cafemambo.dto.PlaceOrderDto;
import lk.cafemambo.service.PlaceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @GetMapping
    public ResponseEntity<?> getPendingOrders(){
        return placeOrderService.getPendingOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetails(@PathVariable String id){
        return placeOrderService.getOrderDetails(id);
    }

    @GetMapping(value = "/dispatch/{id}",produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> dispatchOrder(@PathVariable String id){
        return placeOrderService.dispatchOrder(id);
    }

    @GetMapping(value = "/cancel/{id}",produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> canceledOrder(@PathVariable String id){
        return placeOrderService.canceledOrder(id);
    }
}
