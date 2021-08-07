package com.finalproject.ildoduk.service.review.ServiceInterface;

import com.finalproject.ildoduk.dto.review.ReviewDTO;

import java.util.List;

public interface ReviewService {


    public List<ReviewDTO> getList();

    /*default Review dtoToEntity(ReviewDTO dto){

        Review review=Review.builder().member(Mem_dtoToEntity(dto.getMember())).content(dto.getContent()).biddingList((dto.getDto())).title(dto.getTitle()).build();

        return review;
    }
*/

   /* default ReviewDTO entityToDTO(Review review){

        ReviewDTO dto=ReviewDTO.builder().content(review.getContent()).member(Mem_EntityToDto(review.getMember())).biddingList(review.getBiddingList()).title(review.getTitle()).build();
        return dto;
    }*/


    /*default AuctionDTO auc_entityToDTO(AuctionList auc){

        AuctionDTO auctionListDTO = AuctionDTO.builder().auctionGap(auc.getAuctionGap()).age(auc.getAge()).aucSeq(auc.getAucSeq()).user(Mem_EntityToDto(auc.getUser()))
                .category(auc.getCategory()).content(auc.getContent()).doDateTime(auc.getDoDateTime()).regDate(auc.getRegDate()).driverLicense(auc.getDriverLicense())
                .gender(auc.getGender()).level(auc.getLevel()).predictHour(auc.getPredictHour()).startPrice(auc.getStartPrice()).state(auc.getState())
                .address(auc.getAddress()).sido(auc.getSido()).sigungu(auc.getSigungu())
                .title(auc.getTitle()).aucSeq(auc.getAucSeq()).build();
        return auctionListDTO;
    }

    default MemberDto Mem_EntityToDto(Member entity){

        MemberDto dto = MemberDto.builder()
                .id(entity.getId())
                .pwd(entity.getPwd())
                .name(entity.getName())
                .gender(entity.getGender())
                .birth(entity.getBirth())
                .nickname(entity.getNickname())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .intro(entity.getIntro())
                .state(entity.getState())
                .build();

        return dto;
    }

    default Member Mem_dtoToEntity(MemberDto dto){

        Member entity = Member.builder()
                .id(dto.getId())
                .pwd(dto.getPwd())
                .name(dto.getName())
                .gender(dto.getGender())
                .birth(dto.getBirth())
                .nickname(dto.getNickname())
                .sido(dto.getSido())
                .sigungu(dto.getSigungu())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .point(dto.getPoint())
                .photo(dto.getPhoto())
                .intro(dto.getIntro())
                .state(dto.getState())
                .build();

        return entity;
    }

    default BidDTO Bid_EntityToDto(BiddingList bid){



        BidDTO dto = BidDTO.builder().aucSeq(auc_entityToDTO(bid.getAucSeq())).chosen(bid.getChosen()).helper(Mem_EntityToDto(bid.getHelper())).offerPrice(bid.getOfferPrice()).build();

        return dto;
    }*/

}
