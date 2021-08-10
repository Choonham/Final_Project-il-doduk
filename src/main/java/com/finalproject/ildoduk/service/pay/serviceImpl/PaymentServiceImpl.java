package com.finalproject.ildoduk.service.pay.serviceImpl;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.auction.AuctionBiddingDTO;
import com.finalproject.ildoduk.dto.auction.AuctionListDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.pay.PaymentDTO;
import com.finalproject.ildoduk.entity.auction.AuctionList;
import com.finalproject.ildoduk.entity.auction.BiddingList;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.entity.pay.Payment;
import com.finalproject.ildoduk.repository.auction.AuctionListRepository;
import com.finalproject.ildoduk.repository.auction.BiddingListRepository;
import com.finalproject.ildoduk.repository.pay.PaymentRepository;
import com.finalproject.ildoduk.service.pay.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;


@RequiredArgsConstructor
@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final AuctionListRepository auctionListRepository;
    private final BiddingListRepository biddingListRepository;

    //포인트 충전
    @Override
    public void pointCharge(PaymentDTO dto) {

        Payment entity = dtoToEntity(dto);

        paymentRepository.save(entity);

    }

//----------------------       해당 계정의 결제 이력을 조회한다. -----------------
    //결제 이력 페이지
    @Override
    public PageResultsDTO<PaymentDTO, Payment> getHistory(String test, PageRequestDTO pageRequestDTO) {


        Pageable pageable = pageRequestDTO.getPageable(Sort.by("pointNo").descending());


        Page<Payment> result = paymentRepository.findAllByUserIdId(test,pageable);

        Function<Payment,PaymentDTO> fn = (entity -> entityToDto(entity));

        return new PageResultsDTO<>(result,fn);
    }



//-------------------------        환불            ------------------------
    //환불 창을 위한 정보 꺼내기
    @Override
    public PaymentDTO toRefund(Long pointNo){

        Optional<Payment> result = paymentRepository.findAllByPointNo(pointNo);

        return result.isPresent() ? entityToDto(result.get()) : null;
    }

    //결제 이력 수정
    @Override
    public void updatePayCheck(PaymentDTO dto) {

        //아이디에 해당하는 데이터 조회
        Optional<Payment> result = paymentRepository.findById(dto.getPointNo());

        if(result.isPresent()){
        Payment payment = result.get();
            //값이 존재 한다면 결제이력 y -> n 으로 수정
            payment.updatePayCheck(dto.getPayCheck());

            paymentRepository.save(payment);
        }
    }
//-----------------------  경매 결제 관련  ----------------------
    //1.
    //경매 등록시에 처음 제시 가격만큼 유저 포인트 차감
    @Override
    public void regAuction(Long aucSeq) {
            AuctionList auctionList = auctionListRepository.findById(aucSeq).get();
            paymentRepository.minusPointRegAuction(auctionList.getStartPrice(),auctionList.getUser().getId());
    }

    //경매 매칭 실패시 다시 원래 금액을 반환
    @Override
    public void refundAuctionPay(Long aucSeq) {
            AuctionList auctionList = auctionListRepository.findById(aucSeq).get();
            paymentRepository.plusPointNotMatching(auctionList.getStartPrice(),auctionList.getUser().getId());
    }

    //2.
    //매칭 성공시에 헬퍼가 제시한 가격만큼의 차액 다시 유저 쪽으로
    @Override
    public void biddingSuccess(Long bidSeq) {
        //해당 입찰 정보 얻어내기
        BiddingList biddingList = biddingListRepository.findById(bidSeq).get();
        int offerPrice = biddingList.getOfferPrice();

        AuctionList auctionList = biddingList.getAucSeq();
        int startPrice = auctionList.getStartPrice();

        int difference = (int)(Math.ceil((startPrice - offerPrice)*1.0));
        //아이디와 차액을 전달하여 포인트 증가
        Member member = auctionList.getUser();
        String userId = member.getId();
        paymentRepository.plusPointBidSuccess(difference,userId);
    }
    //3.
    //경매 완료시에 헬퍼 포인트 차감
    //이때 친절 점수에따라 중간 수수료 다르게 계산
    @Override
    public void doneAuction(AuctionBiddingDTO auctionBiddingDTO, MemberDto memberDto) {


        log.info("헬퍼쪽 포인트 업데이트 시작");

        //String userID = dto.getId();
        //들어온 포인트 여기서 조건을 통하여 2가지로 분리 User 리뷰 확인
        // Member의 친절 점수로 : 5점 만점에 3.5이상일 경우 우대 수수료 적용??

        //int point = dto.getPoint();
        int total = 0;

        //친절 점수 들어갈 곳
        String test = null;

        // 중개 수수료 기본 : 10%  -> 0.9
        //    우대 수수료 : 7% -> 0.93

            //우대 수수료 적용
            //total = (int)(Math.ceil(point * 0.93));


            //total = (int)(Math.ceil(point * 0.9));

    }


}
