package com.finalproject.ildoduk.service.auction;

import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.entity.review.Review;
import com.finalproject.ildoduk.repository.review.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
public class ServiceTest {


    @Autowired
    ReviewRepository repository;

    @Test
    void MemberServiceImplTest(){

        Member member = Member.builder().id("pphai@naver.com").build();

        Page<Review> page=repository.findAllById(member, PageRequest.of(10, 10));

        System.out.println(page.getTotalElements());

    }
}
