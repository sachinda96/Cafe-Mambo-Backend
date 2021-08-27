package lk.cafemambo.service;

import lk.cafemambo.dto.ShopOrderDto;
import lk.cafemambo.dto.ShopTableDto;
import org.springframework.http.ResponseEntity;

public interface ShopOrderService {

    public ResponseEntity<?> saveShopTable(ShopTableDto shopTableDto);

    public ResponseEntity<?> placeShopOrder(ShopOrderDto shopOrderDto);

    public ResponseEntity<?> getAllShopTable();

    public ResponseEntity<?> updateLevel(String id);

    public ResponseEntity<?> getAllByUser(String id);

    public ResponseEntity<?> getShopOrder(String id);
}
