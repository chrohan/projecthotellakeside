package com.dailycodework.lakesidehotel.repository;

import com.dailycodework.lakesidehotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("select distinct r.roomType from Room as r")
    List<String> findDistinctRoomTypes();

    @Query("SELECT r FROM Room r WHERE r.roomType LIKE %:roomType% AND r.id NOT IN " +
            "(SELECT br.room.id FROM BookedRoom br WHERE " +
            "(br.checkInDate <= :checkOutDate AND br.checkOutDate >= :checkInDate))")
    List<Room> findByRoomTypeAndAvailability(@Param("checkInDate") LocalDate checkInDate, @Param("checkOutDate") LocalDate checkOutDate, @Param("roomType") String roomType);

}
