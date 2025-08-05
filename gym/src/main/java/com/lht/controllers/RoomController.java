/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.pojo.Room;
import com.lht.services.FacilityService;
import com.lht.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author admin
 */
@Controller
public class RoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private FacilityService facilityService;

    @GetMapping("/rooms")
    public String listRooms(Model model,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        model.addAttribute("rooms", roomService.getAllSort(sortField, sortDir));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "rooms";
    }

    @GetMapping("/rooms/{id}")
    public String getRoom(@PathVariable("id") Integer id, Model model) {
        if  (id != null) {
            model.addAttribute("room", roomService.getRoomById(id));
        }       
        return "rooms"; // Trỏ tới facility.html
    }
    
    @GetMapping("/room/add")
    public String convertRoomForm(@RequestParam(name = "id", required = false) Integer id, Model model) {
        model.addAttribute("room", (id != null) ? roomService.getRoomById(id) : new Room());
        model.addAttribute("facilities", facilityService.getAllFacilities());
        return "room-add"; // Trang form thêm/sửa
    }

    @PostMapping("/room-update")
    public String updateRoom(@ModelAttribute(value = "room") Room p, BindingResult result,
            Model model) {
        if(roomService.addOrUpdateRoom(p) != null) {
            return "redirect:/rooms";
        }
        return "room-add";
    }

    @DeleteMapping("/room-delete/{id}")
    public String destroyRoom(@PathVariable("id") Integer id, Model model) {
        if (id != null) {
            roomService.deleteRoom(id);
        }
        return "redirect:/rooms";
    }
}
