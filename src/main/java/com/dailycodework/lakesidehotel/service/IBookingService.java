package com.dailycodework.lakesidehotel.service;

import com.dailycodework.lakesidehotel.model.BookedRoom;

import java.util.List;

public interface IBookingService {
    void cancelBooking(Long bookingId);

    String saveBooking(Long roomId, BookedRoom bookingRequest) throws Exception;

    BookedRoom findByBookingConfirmationCode(String confirmationcode);

    List<BookedRoom> getAllBooking();
}
