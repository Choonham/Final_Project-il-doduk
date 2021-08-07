package com.finalproject.ildoduk.service.review.Service;

import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.review.ReviewDTO;
import com.finalproject.ildoduk.entity.auction.BiddingList;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.entity.review.Review;
import com.finalproject.ildoduk.repository.auction.BiddingListRepository;
import com.finalproject.ildoduk.repository.member.MemberRepository;
import com.finalproject.ildoduk.repository.review.ReviewRepository;
import com.finalproject.ildoduk.service.review.ServiceInterface.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class ReviewServiceImpl implements ReviewService {
   @Autowired
   ReviewRepository repository;
   @Autowired
    MemberRepository memrepo;
   @Autowired
   BiddingListRepository biding;



    @Override
    public List<ReviewDTO> getList() {
        return null;
    }

    @Override
    public void writeReview(ReviewDTO dto) {

        Member member = Member.builder().id(dto.getId()).build();
        BiddingList biddingList = BiddingList.builder().bidSeq(dto.getBidSeq()).build();
            repository.save(dtoToEntity(dto,member, biddingList));


    }
    @Override
    public ReviewDTO get_ReviewByTitle(String title ){

        Optional<Review> result=repository.findByTitle(title);



        return entityToDto(result.get());

    }
    @Override
    public ReviewDTO get_reviewbyNo(Long bidno){

        Optional<Review> result=repository.findById(bidno);



        return entityToDto(result.get());


    }

}
