/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.Message;
import com.lht.reponsitories.MessageRepository;
import com.lht.services.MessageService;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author admin
 */
@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Message getMessageById(Integer id) {
        return messageRepository.findById(id).orElse(null);
    }

    @Override
    public List<Message> getMessages(Map<String, String> params) {
        Specification<Message> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("senderId")) {
                predicates.add(cb.equal(root.get("sender").get("id"), Integer.parseInt(params.get("senderId"))));
            }
            if (params.containsKey("receiverId")) {
                predicates.add(cb.equal(root.get("receiver").get("id"), Integer.parseInt(params.get("receiverId"))));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return messageRepository.findAll(spec);
    }

    @Override
    public Message addOrUpdateMessage(Message m) {
        return messageRepository.save(m);
    }

    @Override
    public boolean deleteMessage(Integer id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
