package com.project.naeil.board.review.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.naeil.board.review.dao.BoardReviewDAO;
import com.project.naeil.board.review.vo.BoardReviewVO;

@Service("boardReviewService")
public class BoardReviewServiceImpl implements BoardReviewService{

	//의존성
	@Autowired
	private BoardReviewDAO boardReviewDAO;
	
	// 리뷰 전체조회(페이징)
	@Override
	public Map listReview(Map pagingMap) throws Exception {
		Map reviewMap = new HashMap();
		List<BoardReviewVO> reviewList = boardReviewDAO.selectAllReviewList(pagingMap);
			
		// 모든 글 수
		int totalReview = boardReviewDAO.selectTotalReview();
			
		reviewMap.put("reviewList", reviewList);
		reviewMap.put("totalReview", totalReview);
			
		return reviewMap;
	}
	
	//제품 리뷰 조회
	@Override
	public List<BoardReviewVO> listReview(String goods_id) throws Exception {
		List<BoardReviewVO> reviewList = boardReviewDAO.selectReviewList(goods_id);
		return reviewList;
	}


	//리뷰 작성
	@Override
	public int addNewReview(Map reviewMap) throws Exception {
		
		return boardReviewDAO.insertNewReview(reviewMap);
		
	}

	
	//리뷰 삭제
	@Override
	public void removeReviewReview(int review_id) throws Exception {
		
		boardReviewDAO.deleteReview(review_id);
	}


}