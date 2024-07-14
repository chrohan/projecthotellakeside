package com.dailycodework.lakesidehotel.repository;

import com.dailycodework.lakesidehotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("select distinct r.roomType from Room as r")
    List<String> findDistinctRoomTypes();
}
