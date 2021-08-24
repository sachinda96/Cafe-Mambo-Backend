package lk.cafemambo.controller;

import lk.cafemambo.dto.ShopTableDto;
import lk.cafemambo.service.ShopOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/shoporder")
public class ShopOrderController {

    @Autowired
    private ShopOrderService shopOrderService;

    @PostMapping("/savetables")
    public ResponseEntity<?> saveShopTable(@RequestBody ShopTableDto shopTableDto){
        return shopOrderService.saveShopTable(shopTableDto);
    }

    @GetMapping("/getAllTables")
    public ResponseEntity<?> getAllShopTable(){
        return shopOrderService.getAllShopTable();
    }

    @GetMapping("/updateLevel/{id}")
    public ResponseEntity<?> updateLevel(@PathVariable String id){
        return shopOrderService.updateLevel(id);
    }

    @GetMapping("/getAllUserRole/{id}")
    public ResponseEntity<?> getAllUserRole(@PathVariable String id){
        return shopOrderService.updateLevel(id);
    }

    @GetMapping("/getAllByUser/{id}")
    public ResponseEntity<?> getAllByUser(@PathVariable String id){
        return shopOrderService.getAllByUser(id);
    }

    @GetMapping("/getShopOrder/{id}")
    public ResponseEntity<?> getShopOrder(@PathVariable String id){
        return shopOrderService.getShopOrder(id);
    }
}
