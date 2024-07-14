package com.dailycodework.lakesidehotel.repository;

import com.dailycodework.lakesidehotel.model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<BookedRoom,Long> {

    BookedRoom findByBookingConfirmationCode(String confirmationCode);
}
