package com.buysellgo.reviewservice.strategy.impl;

import com.buysellgo.reviewservice.strategy.common.ReviewStrategy;
import java.util.Map;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import com.buysellgo.reviewservice.common.entity.Role;
import com.buysellgo.reviewservice.controller.dto.ReviewCreateReq;
import com.buysellgo.reviewservice.strategy.common.ReviewResult;
import com.buysellgo.reviewservice.entity.Review;
import com.buysellgo.reviewservice.repository.ReviewRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.buysellgo.reviewservice.common.util.CommonConstant.*;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional  
public class AdminReviewStrategy implements ReviewStrategy<Map<String, Object>> {
    private final ReviewRepository reviewRepository;
    @Override
    public ReviewResult<Map<String, Object>> getReview(long userId) { 
        Map<String, Object> data = new HashMap<>();
        List<Review> reviews = reviewRepository.findAll();
        if(reviews.isEmpty()){
            data.put(REVIEW_VO.getValue(), null);
            return ReviewResult.fail(REVIEW_NOT_FOUND.getValue(), data);
        }
        List<Review.Vo> reviewVos = reviews.stream().map(Review::toVo).toList();
        data.put(REVIEW_VO.getValue(), reviewVos);
        return ReviewResult.success(REVIEW_GET_SUCCESS.getValue(), data);
    }

    @Override
    public ReviewResult<Map<String, Object>> writeReview(ReviewCreateReq req, long userId) {
        // 리뷰 작성 권한은 회원만 함
        return null;
    }

    @Override
    public ReviewResult<Map<String, Object>> updateReview(ReviewCreateReq req, long userId) {
        // 리뷰 수정 권한은 회원과 관리자만 함
        return null;
    }

    @Override
    public ReviewResult<Map<String, Object>> deleteReview(long reviewId, long userId) {
        Map<String, Object> data = new HashMap<>();
        Optional<Review> review = reviewRepository.findById(reviewId);
        if(review.isEmpty()){
            data.put(REVIEW_VO.getValue(), null);
            return ReviewResult.fail(REVIEW_NOT_FOUND.getValue(), data);
        }
        reviewRepository.delete(review.get());
        data.put(REVIEW_VO.getValue(), review.get().toVo());
        return ReviewResult.success(REVIEW_DELETE_SUCCESS.getValue(), data);
    }

    @Override
    public boolean supports(Role role) {
        return role == Role.ADMIN;      
    }

}
