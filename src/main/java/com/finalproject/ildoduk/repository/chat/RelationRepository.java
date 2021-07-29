package com.finalproject.ildoduk.repository.chat;


import com.finalproject.ildoduk.entity.chat.Relation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationRepository extends JpaRepository<Relation,Long> {

    public Relation findBySend(String send);



}
