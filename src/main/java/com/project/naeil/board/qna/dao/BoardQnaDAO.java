package com.project.naeil.board.qna.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;


import com.project.naeil.board.qna.vo.BoardQnaVO;

public interface BoardQnaDAO {
	
	//총 QNA 전체조회
	public List selectAllQnaList(Map pagingMap) throws DataAccessException;
		
	//총 글 수
	public int selectTotalQna() throws DataAccessException;
	
	//한 제품 QNA 전체조회
	public List selectQnaList(String goods_id) throws DataAccessException;
	
	//QNA 작성
	public int insertNewQna(Map qnaMap) throws DataAccessException;
	
	
	//QNA 상세
	public BoardQnaVO viewQna(int notice_id) throws DataAccessException;
		
	//QNA 수정
	public void updateQna(Map qnaMap) throws DataAccessException;
	
	//QNA 삭제
	public void deleteQna(int qna_id) throws DataAccessException;
}