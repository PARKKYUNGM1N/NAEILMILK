package com.project.naeil.board.review.service;

import java.util.List;
import java.util.Map;

import com.project.naeil.board.review.vo.BoardReviewVO;

public interface BoardReviewService {
	
	//리뷰 전체조회(페이징)
	public Map listReview(Map pagingMap) throws Exception;
	
	//리뷰 조회
	public List<BoardReviewVO> listReview(String goods_id) throws Exception;

	//리뷰 작성
	public int addNewReview(Map reviewMap) throws Exception;
	
	//리뷰삭제
	public void removeReviewReview(int review_id) throws Exception;


}