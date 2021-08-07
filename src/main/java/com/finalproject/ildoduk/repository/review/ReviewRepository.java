package com.finalproject.ildoduk.repository.review;

import com.finalproject.ildoduk.dto.review.ReviewDTO;
import com.finalproject.ildoduk.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    public List<Review> findAll();






}
