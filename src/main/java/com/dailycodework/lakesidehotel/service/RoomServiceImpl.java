package com.dailycodework.lakesidehotel.service;

import com.dailycodework.lakesidehotel.model.Room;
import com.dailycodework.lakesidehotel.repository.BookingRepository;
import com.dailycodework.lakesidehotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements IRoomService{

    private final RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws SQLException, IOException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        if(!file.isEmpty()){
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            room.setPhoto(photoBlob);
        }
        return roomRepository.save(room);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes) {
        Room room = roomRepository.findById(roomId).orElse(null);

        if(roomType != null) room.setRoomType(roomType);
        if(roomPrice != null) room.setRoomPrice(roomPrice);
        if(photoBytes != null && photoBytes.length > 0){
            try{
                room.setPhoto(new SerialBlob(photoBytes));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return roomRepository.save(room);
    }

    @Override
    public List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        List<Room> availableRooms = roomRepository.findByRoomTypeAndAvailability(checkInDate,checkOutDate,roomType);
        return availableRooms;
    }
}
