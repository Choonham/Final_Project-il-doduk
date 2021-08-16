package com.finalproject.ildoduk.webSocket;

import com.finalproject.ildoduk.entity.chat.Chat;

import java.util.Comparator;

public class ChatCompare implements Comparator<Chat> {
    @Override
    public int compare(Chat c1, Chat c2) {
        if(c1.getNo()>c2.getNo()){
        return 1;
        }
        if(c1.getNo()<c2.getNo()){
            return -1;
        }



        return 0;
    }
}
