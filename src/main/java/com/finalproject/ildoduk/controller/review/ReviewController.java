package com.finalproject.ildoduk.controller.review;


import com.finalproject.ildoduk.service.review.ServiceInterface.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    ReviewService service;
        
    /*=======단순히 리스트만=====*/
    @GetMapping("/reviewList")
    public void reviewList(Model model){


    model.addAttribute("list",service.getList());


    }



}
