package com.project.naeil.board.qna.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.project.naeil.board.qna.vo.BoardQnaVO;
import com.project.naeil.board.review.vo.BoardReviewVO;

@Repository("boardQnaDAO")
public class BoardQnaDAOImpl implements BoardQnaDAO{
	
	//의존성
	@Autowired
	private SqlSession sqlSession;
	
	// 리뷰전체 조회
	@Override
	public List selectAllQnaList(Map pagingMap) throws DataAccessException {
		List<BoardQnaVO> qnaList = sqlSession.selectList("mapper.board_qna.selectAllQna", pagingMap);
				
		return qnaList;
	}
			
	// 페이징에 필요한 전체 개수
	@Override
	public int selectTotalQna() throws DataAccessException {
		int totalQna = sqlSession.selectOne("mapper.board_qna.selectTotalQna");
				
		return totalQna;
	}
		
	//한 제품 QNA 전체조회
	@Override
	public List selectQnaList(String goods_id) throws DataAccessException {
		List<BoardQnaVO> qnaList = sqlSession.selectList("mapper.board_qna.selectQnaList",goods_id);
		return qnaList;
	}
	
	// 공지 조회
	@Override
	public BoardQnaVO viewQna(int qna_id) throws DataAccessException {
		BoardQnaVO qnaVO = (BoardQnaVO) sqlSession.selectOne("mapper.board_qna.viewQna", qna_id);

		return qnaVO;
	}
	
	
	//QNA 작성
	@Override
	public int insertNewQna(Map qnaMap) throws DataAccessException {
		int qna_id = selectMaxQnaId();
		qnaMap.put("review_id", qna_id);
		sqlSession.insert("mapper.board_qna.insertNewQna", qnaMap);
		return qna_id;
	}
	
	
	//QNA 삭제
	@Override
	public void deleteQna(int qna_id) throws DataAccessException{
		sqlSession.delete("mapper.board_qna.deleteQna", qna_id);
				
	}
	
	//QNA 수정
	@Override
	public void updateQna(Map qnaMap) throws DataAccessException {
		sqlSession.update("mapper.board_qna.updateQna", qnaMap);
		
	}
	
	private int selectMaxQnaId() throws DataAccessException{
		int qna_id =sqlSession.selectOne("mapper.board_qna.selectMaxQnaId");
		return qna_id;
	}

	

}