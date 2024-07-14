package com.dailycodework.lakesidehotel.repository;

import com.dailycodework.lakesidehotel.model.BookedRoom;
import com.dailycodework.lakesidehotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookedRoom,Long> {

    BookedRoom findByBookingConfirmationCode(String confirmationCode);




}
