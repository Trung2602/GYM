/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services;

import com.lht.pojo.Message;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin
 */
public interface MessageService {

    List<Message> getMessages(Map<String, String> params);

    List<Message> getAllMessages();

    Message getMessageById(Integer id);

    Message addOrUpdateMessage(Message m);

    boolean deleteMessage(Integer id);
}
