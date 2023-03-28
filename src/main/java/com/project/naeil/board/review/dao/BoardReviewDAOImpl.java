package com.project.naeil.board.review.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.project.naeil.board.review.vo.BoardReviewVO;

@Repository("boardReviewDAO")
public class BoardReviewDAOImpl implements BoardReviewDAO{

	//의존성
	@Autowired
	private SqlSession sqlSession;
	
	// 리뷰전체 조회
	@Override
	public List selectAllReviewList(Map pagingMap) throws DataAccessException {
		List<BoardReviewVO> reviewList = sqlSession.selectList("mapper.board_review.selectAllReview", pagingMap);
			
		return reviewList;
	}
			
	// 페이징에 필요한 전체 개수
	@Override
	public int selectTotalReview() throws DataAccessException {
		int totalReview = sqlSession.selectOne("mapper.board_review.selectTotalReview");
			
		return totalReview;
	}
	
	//리뷰 조회
	@Override
	public List selectReviewList(String goods_id) throws DataAccessException {
		List<BoardReviewVO> reviewList = sqlSession.selectList("mapper.board_review.selectReviewList",goods_id);
		return reviewList;
	}

	
	//리뷰 작성
	@Override
	public int insertNewReview(Map reviewMap) throws DataAccessException {
		int review_id = selectMaxReviewId();
		reviewMap.put("review_id", review_id);
		sqlSession.insert("mapper.board_review.insertNewReview", reviewMap);
		return review_id;
	}

	
	private int selectMaxReviewId() throws DataAccessException{
		int review_id =sqlSession.selectOne("mapper.board_review.selectMaxReviewId");
		return review_id;
	}
	
	
	//리뷰 삭제
	@Override
	public void deleteReview(int review_id) throws DataAccessException{
		sqlSession.delete("mapper.board_review.deleteReview", review_id);
			
	}
	
}