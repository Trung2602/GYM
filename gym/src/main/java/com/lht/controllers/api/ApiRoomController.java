/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.pojo.Room;
import com.lht.services.RoomService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/api")
public class ApiRoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping("/rooms-all")
    public ResponseEntity<List<Room>> getRoomsAll() {
        return ResponseEntity.ok(this.roomService.getAllRooms());
    }
    
    @GetMapping("/rooms-filter")
    public ResponseEntity<List<Room>> getRoomsFilter(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(this.roomService.getRooms(params));
    }

    @GetMapping("rooms-sort")
    public ResponseEntity<List<Room>> getRoomsSort(
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(this.roomService.getAllSort(sortField, sortDir));
    }
    
    @GetMapping("room/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable("id") Integer id) {   
        if (id == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(this.roomService.getRoomById(id));
    }

    @PostMapping("room-update")
    public ResponseEntity<Room> addOrUpdateRoom(@RequestBody Room room) {
        return ResponseEntity.ok(this.roomService.addOrUpdateRoom(room));
    }

    @DeleteMapping("room-delete/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable("id") Integer id) {
        if (this.roomService.deleteRoom(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
