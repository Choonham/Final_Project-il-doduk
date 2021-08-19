package com.finalproject.ildoduk.service.chat;


import com.finalproject.ildoduk.dto.chat.ChatAucDTO;
import com.finalproject.ildoduk.dto.chat.ChatDTO;
import com.finalproject.ildoduk.entity.auction.AuctionList;
import com.finalproject.ildoduk.entity.chat.Chat;
import com.finalproject.ildoduk.entity.member.Member;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ChatService {

    public void message_send(ChatDTO dto);
    public List<ChatDTO> get_chatList(Member userID, String id,Long auc);
    public List<ChatAucDTO> get_chatUI(String user);
    public Map<String ,Long> get_count(String user);

    default Chat dtoToEntity(ChatDTO dto){
        Member member =Member.builder().id(dto.getRecive()).build();
        AuctionList auction = AuctionList.builder().aucSeq(dto.getAuc_seq()).build();
        Chat chat =Chat.builder().reciver(member).sender(dto.getSender()).message(dto.getMessage()).time(dto.getTime()).list(auction).build();

        return chat;
    }
    default ChatDTO entityToDto(Chat chat){

        ChatDTO result = ChatDTO.builder().No(chat.getNo()).recive(chat.getReciver().getId())
                .sender(chat.getSender())
                .time(chat.getTime())
                .message(chat.getMessage()).auc_seq(chat.getList().getAucSeq()).build();
        return result;
    }




}
