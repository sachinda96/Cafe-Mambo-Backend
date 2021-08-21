package lk.cafemambo.service;

import lk.cafemambo.dto.ItemDto;
import lk.cafemambo.dto.ReviewDto;
import org.springframework.http.ResponseEntity;

public interface ItemReviewService {

    public ResponseEntity<?> save(ReviewDto reviewDto);

    public ResponseEntity<?> getAllByItem(String itemId);

}
