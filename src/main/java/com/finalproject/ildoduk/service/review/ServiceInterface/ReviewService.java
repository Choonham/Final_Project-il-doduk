package com.finalproject.ildoduk.service.review.ServiceInterface;

import com.finalproject.ildoduk.dto.review.ReviewDTO;
import com.finalproject.ildoduk.entity.review.Review;

import java.util.List;

public interface ReviewService {

    public List<ReviewDTO> getList();

    default Review dtoToEntity(ReviewDTO dto){

        Review review=Review.builder().aucSeq(dto.getAucSeq()).content(dto.getContent())
                .biddingList(dto.getBiddingList()).helperInfo(dto.getHelperInfo())
                .client(dto.getClient()).title(dto.getTitle()).build();

        return review;
    }


    default ReviewDTO entityToDTO(Review review){

        ReviewDTO dto=ReviewDTO.builder().aucSeq(review.getAucSeq()).content(review.getContent())
                .biddingList(review.getBiddingList()).helperInfo(review.getHelperInfo())
                .client(review.getClient()).title(review.getTitle()).build();

        return dto;
    }
}
