package lk.cafemambo.controller;

import lk.cafemambo.dto.ReviewDto;
import lk.cafemambo.service.ItemReviewService;
import lk.cafemambo.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/item")
public class ItemReviewController {

    @Autowired
    private ItemReviewService itemReviewService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ReviewDto reviewDto){
        return itemReviewService.save(reviewDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<?> getAllByItem(@PathVariable String itemId){
        return itemReviewService.getAllByItem(itemId);
    }
}
