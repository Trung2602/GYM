/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.Room;
import com.lht.reponsitories.RoomRepository;
import com.lht.services.RoomService;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author admin
 */
@Service
@Transactional
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Room getRoomById(Integer id) {
        return roomRepository.findById(id).orElse(null);
    }

    @Override
    public List<Room> getRooms(Map<String, String> params) {
        Specification<Room> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("facilityId")) {
                predicates.add(cb.equal(root.get("facilityId").get("id"),Integer.parseInt(params.get("facilityId"))));
            }

            if (params.containsKey("roomNumber")) {
                predicates.add(cb.like(cb.lower(root.get("roomNumber")), "%" + params.get("roomNumber").toLowerCase() + "%" ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return roomRepository.findAll(spec);
    }

    @Override
    public Room addOrUpdateRoom(Room r) {
        return roomRepository.save(r);
    }

    @Override
    public boolean deleteRoom(Integer id) {
        if (roomRepository.existsById(id)) {
            roomRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    @Override
    public List<Room> getAllSort(String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        return roomRepository.findAll(sort);
    }
}
