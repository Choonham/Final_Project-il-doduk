package com.finalproject.ildoduk.service.chat;


import com.finalproject.ildoduk.dto.chat.ChatDTO;
import com.finalproject.ildoduk.entity.chat.Chat;

import java.util.List;

public interface ChatService {

    public void message_send(ChatDTO dto);
    public List<ChatDTO> get_chatList(String userID, String id);


    default Chat dtoToEntity(ChatDTO dto){

        Chat chat =Chat.builder().reciver(dto.getTo()).sender(dto.getFrom()).message(dto.getMessage()).time(dto.getTime()).build();

        return chat;
    }




    default ChatDTO entityToDto(Chat chat){


        ChatDTO dto = ChatDTO.builder().to(chat.getReciver()).from(chat.getSender()).message(chat.getMessage()).time(chat.getTime()).build();

        return dto;
    }
}
