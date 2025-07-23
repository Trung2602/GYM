/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services;

import com.lht.pojo.Room;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin
 */
public interface RoomService {

    List<Room> getRooms(Map<String, String> params);

    List<Room> getAllRooms();

    Room getRoomById(Integer id);

    Room addOrUpdateRoom(Room r);

    boolean deleteRoom(Integer id);
}
