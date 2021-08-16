package com.finalproject.ildoduk.service.chat;

import com.finalproject.ildoduk.dto.chat.ChatAucDTO;
import com.finalproject.ildoduk.dto.chat.ChatDTO;
import com.finalproject.ildoduk.entity.auction.AuctionList;
import com.finalproject.ildoduk.entity.chat.Chat;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.repository.chat.ChatRepository;
import com.finalproject.ildoduk.webSocket.ChatCompare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatServiceImpl implements ChatService{

    @Autowired
    ChatRepository repo;


    public List<ChatDTO> get_chatList(Member userID, String id,Long auc){


    Member member=Member.builder().id(id).build();
        AuctionList list = AuctionList.builder().aucSeq(auc).build();
    List<Chat> list_final= new ArrayList<>();
    List<Chat> list_to=repo.findAllBySenderAndReciverAndList(id,userID,list);
    List<Chat> list_from=repo.findAllBySenderAndReciverAndList(userID.getId(),member,list);
    list_final.addAll(list_to);
    list_final.addAll(list_from);
    Collections.sort(list_final, new ChatCompare());
    List<ChatDTO> dtoList = new ArrayList<>();
        for (Chat chat: list_final
             ) {
            ChatDTO dto = entityToDto(chat);
            dtoList.add(dto);


        }
        //읽음 처리
        for (Chat chat: list_final) {

            chat.changeState();
            repo.save(chat);
        }



    return dtoList;

    }

    @Override
    public List<ChatAucDTO> get_chatUI(String user) {

        List<Object[]> list=repo.getChatList(user);
        List<ChatAucDTO> ch_li= new ArrayList<>();
        for (Object c: list
        ) {

            Object [] result =(Object[]) c;
            //long
            System.out.println(result[0]);
            //long max
            System.out.println(result[1]);
            //message
            System.out.println(result[2]);
            //reciver
            System.out.println(result[3]);
            //time

            System.out.println(result[4]);

            ChatAucDTO dto = new ChatAucDTO();
            dto.setDate((String)result[4]);
            dto.setMessage((String) result[2]);
            dto.setRecive((String)result[3]);
            dto.setImg((String)result[5]);
            dto.setAuc_sec((Long) result[0]);
            ch_li.add(dto);
        }




        return ch_li;
    }

    @Override
    public Map<String, Long> get_count(String user) {
        List<Object[]> list=repo.getCount("vhdvhd0307@naver.com");
        Map<String ,Long> map = new HashMap<>();
        for (Object c: list) {
            Object [] result =(Object[]) c;
            map.put((String) result[1],(Long) result[0]);


        }


        return map;
    }

    @Override
    public void message_send(ChatDTO dto) {

     Chat chat=dtoToEntity(dto);
        System.out.println(chat.getMessage());
     repo.save(chat);
    }


}
