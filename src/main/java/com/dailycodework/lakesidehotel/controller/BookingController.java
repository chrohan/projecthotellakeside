package com.dailycodework.lakesidehotel.controller;

import com.dailycodework.lakesidehotel.model.BookedRoom;
import com.dailycodework.lakesidehotel.model.Room;
import com.dailycodework.lakesidehotel.repository.BookingRepository;
import com.dailycodework.lakesidehotel.repository.RoomRepository;
import com.dailycodework.lakesidehotel.response.BookingResponse;
import com.dailycodework.lakesidehotel.response.RoomResponse;
import com.dailycodework.lakesidehotel.service.IBookingService;
import com.dailycodework.lakesidehotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private IBookingService bookingService;

    @Autowired
    private IRoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

   @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings(){
        List<BookedRoom> bookings = bookingService.getAllBooking();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for(BookedRoom booking : bookings){
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    private BookingResponse getBookingResponse(BookedRoom booking) {
       Room theRoom = roomRepository.findById(booking.getRoom().getId()).get();

        RoomResponse room = new RoomResponse (
            theRoom.getId(),
            theRoom.getRoomType(),
            theRoom.getRoomPrice()
        );
       return new BookingResponse(booking.getBookingId(),booking.getCheckInDate(),
               booking.getCheckOutDate(),booking.getGuestFullName(),
               booking.getGuestEmail(),booking.getNumOfAdults(),
               booking.getNumOfChildren(),
               booking.getTotalNumOfGuest(),booking.getBookingConfirmationCode(),
               room);

    }

    @GetMapping("/confirmation/{confirmationcode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationcode){
       try{
           BookedRoom booking = bookingService.findByBookingConfirmationCode(confirmationcode);
           BookingResponse bookingResponse = getBookingResponse(booking);
           return ResponseEntity.ok(bookingResponse);

       }catch ( Exception Invalid){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Invalid.getMessage());

       }
    }
    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long roomId,
                                         @RequestBody BookedRoom bookingRequest){
       try{
           String confirmationCode = bookingService.saveBooking(roomId,bookingRequest);
           return  ResponseEntity.ok(
                    "Room booked successfully : Your booking confirmation code is :" + confirmationCode);
       }catch(Exception InvalidBooking){
           return ResponseEntity.badRequest().body("InvalidBooking");

       }
    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking(@PathVariable Long bookingId){
       bookingService.cancelBooking(bookingId);
    }

    @GetMapping("/findroom/bookingId")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long bookingId){

       BookedRoom bookedRoom = bookingRepository.findById(bookingId).get();

       return ResponseEntity.ok(getBookingResponse(bookedRoom));

    }




}
