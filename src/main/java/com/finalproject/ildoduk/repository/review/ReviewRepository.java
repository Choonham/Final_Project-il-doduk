package com.finalproject.ildoduk.repository.review;

import com.finalproject.ildoduk.dto.review.ReviewDTO;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.entity.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {


    Optional<Review> findByTitle(String title);


    Page<Review> findAllByBidSeq_Helper(Member id, Pageable pageable);
}
