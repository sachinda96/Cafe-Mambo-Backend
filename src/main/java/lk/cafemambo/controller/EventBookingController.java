package lk.cafemambo.controller;

import lk.cafemambo.dto.EventBookingDto;
import lk.cafemambo.service.EventBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/eventbooking")
public class EventBookingController {

    @Autowired
    private EventBookingService eventBookingService;


    @PostMapping(value = "/save")
    public ResponseEntity<?> addBooking(EventBookingDto eventBookingDto){
        return eventBookingService.addBooking(eventBookingDto);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<?> updateBooking(EventBookingDto eventBookingDto){
        return eventBookingService.updateBooking(eventBookingDto);
    }

    @GetMapping(value = "/remove/{id}")
    public ResponseEntity<?> removeBooking(@PathVariable String id){
        return eventBookingService.removeBooking(id);
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<?> getBooking(@PathVariable String id){
        return eventBookingService.getBooking(id);
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<?> getAllBooking(){
        return eventBookingService.getAllBooking();
    }

    @GetMapping(value = "/getAllPending")
    public ResponseEntity<?> getAllPendingBooking(){
        return eventBookingService.getAllPendingBooking();
    }


}
