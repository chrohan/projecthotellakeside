package com.dailycodework.lakesidehotel.controller;

import com.dailycodework.lakesidehotel.model.Room;
import com.dailycodework.lakesidehotel.repository.RoomRepository;
import com.dailycodework.lakesidehotel.response.RoomResponse;
import com.dailycodework.lakesidehotel.service.IRoomService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private IRoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @PostMapping("/add/new-room")
    public ResponseEntity<RoomResponse> addNewRoom(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType")String roomType,
            @RequestParam("roomPrice")BigDecimal roomPrice) throws SQLException, IOException {

        Room savedRoom = roomService.addNewRoom(photo,roomType,roomPrice);
        RoomResponse  response = new RoomResponse(savedRoom.getId(),
                savedRoom.getRoomType(), savedRoom.getRoomPrice());

        return ResponseEntity.ok(response);

    }

    @GetMapping("/get-rooms")
    @Transactional
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
        List<Room> rooms = roomRepository.findAll();
        List<RoomResponse>  roomResponses = new ArrayList<>();
         for(Room room:rooms){
             Room temp = roomRepository.findById(room.getId()).orElse(null);
             Blob photo = temp.getPhoto();
             byte[] photoBytes = null;
             if(photo!=null){
                 photoBytes = photo.getBytes(1,(int)photo.length());
             }
             RoomResponse response = new RoomResponse(room.getId(),room.getRoomType(),room.getRoomPrice());
             response.setPhoto(Base64.encodeBase64String(photoBytes));
             roomResponses.add(response);
         }
         return ResponseEntity.ok(roomResponses);
    }

     @GetMapping("/room/types")
  public ResponseEntity<List<String>> getAllRoomTypes() {
       List<String> roomtypes = roomService.getAllRoomTypes();
        return ResponseEntity.ok(roomtypes);
  }

  @DeleteMapping("/delete/room/{roomId}")
  public ResponseEntity<Void> deleteRoom(@PathVariable("roomId") Long id){
        roomRepository.deleteById(id);
        return ResponseEntity.ok().build();
  }

  @PutMapping("/update/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,
                                                   @RequestParam(required = false) String roomType,
                                                   @RequestParam(required = false) BigDecimal roomPrice,
                                                   @RequestParam(required = false) MultipartFile photo) throws SQLException, IOException {
        byte[] photoBytes = photo != null && !photo.isEmpty() ?
                photo.getBytes() : null;

        Blob photoBlob = photoBytes != null && photoBytes.length > 0 ? new SerialBlob(photoBytes) : null;

        Room theRoom = roomService.updateRoom(roomId, roomType, roomPrice, photoBytes);
        theRoom.setPhoto(photoBlob);
        RoomResponse roomResponse = new RoomResponse(theRoom.getId(),theRoom.getRoomType(),theRoom.getRoomPrice());
        roomResponse.setPhoto(Base64.encodeBase64String(photoBytes));

        return ResponseEntity.ok(roomResponse);
  }

  @GetMapping("/room/{roomId}")
  public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long roomId){
        Room tempRoom = roomRepository.findById(roomId).orElse(null);
        RoomResponse roomResponse =  new RoomResponse(tempRoom.getId(),tempRoom.getRoomType(),tempRoom.getRoomPrice());
        return ResponseEntity.ok(roomResponse);

  }

    public ResponseEntity<List<RoomResponse>> getAvailableRooms(
            @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam("roomType") String roomType) throws SQLException {
        List<Room> availableRooms = roomService.getAvailableRooms(checkInDate, checkOutDate, roomType);
        List<RoomResponse> availableRoomResponses = new ArrayList<>();
        for(Room room : availableRooms){
            Blob photo = room.getPhoto();
            byte[] photoBytes = null;
            if(photo!=null){
                photoBytes = photo.getBytes(1,(int)photo.length());
            }
            if(photoBytes != null && photoBytes.length > 0){
                String photoBase64 = Base64.encodeBase64String(photoBytes);
                RoomResponse roomResponse = new RoomResponse(room.getId(),room.getRoomType(),room.getRoomPrice());
                roomResponse.setPhoto(photoBase64);
                availableRoomResponses.add(roomResponse);
            }
        }
        if(availableRoomResponses.isEmpty()){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(availableRoomResponses);
        }
    }
}
