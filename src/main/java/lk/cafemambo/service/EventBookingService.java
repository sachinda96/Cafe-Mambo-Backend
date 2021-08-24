package lk.cafemambo.service;

import lk.cafemambo.dto.EventBookingDto;
import org.springframework.http.ResponseEntity;

public interface EventBookingService {

    public ResponseEntity<?> addBooking(EventBookingDto eventBookingDto);

    public ResponseEntity<?> updateBooking(EventBookingDto eventBookingDto);

    public ResponseEntity<?> removeBooking(String id);

    public ResponseEntity<?> getBooking(String id);

    public ResponseEntity<?> getAllBooking();

    public ResponseEntity<?> getAllPendingBooking();

    public ResponseEntity<?> completeEvent(String id);

}
