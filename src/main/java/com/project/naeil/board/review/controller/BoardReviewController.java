package com.project.naeil.board.review.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

public interface BoardReviewController {

	
	//리뷰 전체조회(페이징)
	public ModelAndView listReview(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	//리뷰 작성
	public ResponseEntity addNewReview(HttpServletRequest request, HttpServletResponse response) throws Exception;

	
	
	//리뷰삭제
	public void removeReview(HttpServletRequest request, HttpServletResponse response,
							 @RequestParam(value = "review_id", required = false) int review_id) throws Exception;
}