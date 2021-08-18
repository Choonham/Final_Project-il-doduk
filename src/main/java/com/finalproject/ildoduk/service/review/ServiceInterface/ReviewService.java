package com.finalproject.ildoduk.service.review.ServiceInterface;

import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.review.RequestDto;
import com.finalproject.ildoduk.dto.review.ResultDto;
import com.finalproject.ildoduk.dto.review.ReviewDTO;
import com.finalproject.ildoduk.entity.auction.BiddingList;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.entity.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.swing.text.html.parser.Entity;
import java.util.List;

public interface ReviewService {

    public void delete(String no);
    public ResultDto<ReviewDTO,Review> getList(RequestDto dto,String id);

    public void writeReview(ReviewDTO dto);

    public ReviewDTO get_ReviewByTitle(String title );

    public ReviewDTO get_reviewbyNo(Long bidno);

    public List<ReviewDTO> get_LIst(Pageable pageable);

    public ReviewDTO get_reviewdtobyprimary(Long no);
    default Review dtoToEntity(ReviewDTO dto, Member member, BiddingList bid){

        Review result = Review.builder().content(dto.getContent()).title(dto.getTitle()).id(member).bidSeq(bid).build();

        return result;

    }
    default ReviewDTO entityToDto(Review review){

    ReviewDTO result = ReviewDTO.builder().bidSeq(review.getBidSeq().getBidSeq())
            .id(review.getId().getId()).content(review.getContent()).no(review.getNo())
            .title(review.getTitle()).build();
        return result;
    }



}
