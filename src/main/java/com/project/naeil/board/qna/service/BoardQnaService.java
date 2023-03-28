package com.project.naeil.board.qna.service;

import java.util.List;
import java.util.Map;

import com.project.naeil.board.qna.vo.BoardQnaVO;


public interface BoardQnaService {
	
	
	//QNA 전체조회(페이징)
	public Map listQna(Map pagingMap) throws Exception;
	
	//한 제품 QNA 전체조회
	public List<BoardQnaVO> listQna(String goods_id) throws Exception;

	//QNA 작성
	public int addNewQna(Map qnaMap) throws Exception;
	
	
	//QNA 상세
	public Map viewQna(int qna_id) throws Exception;
	
	//QNA 수정
	public void updateQna(Map qnaMap) throws Exception;
	
	//QNA 삭제
	public void removeQna(int qna_id) throws Exception;
	
}