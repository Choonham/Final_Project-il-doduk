package com.finalproject.ildoduk.service.review.Service;

import com.finalproject.ildoduk.dto.review.ReviewDTO;
import com.finalproject.ildoduk.entity.review.Review;
import com.finalproject.ildoduk.repository.review.ReviewRepository;
import com.finalproject.ildoduk.service.review.ServiceInterface.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewRepository repo;


    /*======단순히 읽어오는 부분=====*/
    public List<ReviewDTO> getList(){
        List<Review> list=repo.findAll();

        List<ReviewDTO> dtoList = new ArrayList<>();

        for (Review review: list)
        {
            ReviewDTO dto = new ReviewDTO();

            dto= entityToDTO(review);
            dtoList.add(dto);

        }
        return dtoList;

    }


}
