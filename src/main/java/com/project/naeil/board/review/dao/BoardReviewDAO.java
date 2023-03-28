package com.project.naeil.board.review.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

public interface BoardReviewDAO {
	
	//리뷰 전체조회
	public List selectAllReviewList(Map pagingMap) throws DataAccessException;
		
	//총 글 수
	public int selectTotalReview() throws DataAccessException;
	
	//리뷰 조회
	public List selectReviewList(String goods_id) throws DataAccessException;
	
	//리뷰 작성
	public int insertNewReview(Map reviewMap) throws DataAccessException;
	
	//리뷰 삭제
	public void deleteReview(int review_id) throws DataAccessException;
}