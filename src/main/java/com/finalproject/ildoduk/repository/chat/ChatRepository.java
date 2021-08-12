package com.finalproject.ildoduk.repository.chat;

import com.finalproject.ildoduk.entity.auction.AuctionList;
import com.finalproject.ildoduk.entity.chat.Chat;
import com.finalproject.ildoduk.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat,Long> {
    public List<Chat> findAllBySenderAndReciverAndList(String sender, Member reciver, AuctionList auc);

    @Query(value = "select distinct(c.list.aucSeq) ,max(c.No),c.message,c.reciver.id,c.time,c.reciver.photo from Chat c where c.sender=:user order by c.No asc")
    public List<Object[]> getChatList(String user);

}
