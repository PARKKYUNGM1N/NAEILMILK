package com.project.naeil.board.qna.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.naeil.board.qna.dao.BoardQnaDAO;
import com.project.naeil.board.qna.vo.BoardQnaVO;
import com.project.naeil.board.review.vo.BoardReviewVO;

@Service("boardQnaService")
public class BoardQnaServiceImpl implements BoardQnaService{

	//의존성
	@Autowired
	private BoardQnaDAO boardQnaDAO;

	
	// QNA 전체조회(페이징)
	@Override
	public Map listQna(Map pagingMap) throws Exception {
		Map qnaMap = new HashMap();
		List<BoardQnaVO> qnaList = boardQnaDAO.selectAllQnaList(pagingMap);
				
		// 모든 글 수
		int totalQna = boardQnaDAO.selectTotalQna();
				
		qnaMap.put("qnaList", qnaList);
		qnaMap.put("totalQna", totalQna);
				
		return qnaMap;
	}
	
	
	
	//한 제품 QNA 전체조회
	@Override
	public List<BoardQnaVO> listQna(String goods_id) throws Exception {
		List<BoardQnaVO> qnaList = boardQnaDAO.selectQnaList(goods_id);
		return qnaList;
	}
	
	
	//QNA 상세
	@Override
	public Map viewQna(int qna_id) throws Exception {
			
		Map qnaMap = new HashMap();
			
		BoardQnaVO qnaVO = boardQnaDAO.viewQna(qna_id);
		
		qnaMap.put("qnaVO", qnaVO);
			
			
		return qnaMap;
	}
	
	
	//Qna 작성
	@Override
	public int addNewQna(Map qnaMap) throws Exception {
		int qna_id = boardQnaDAO.insertNewQna(qnaMap);
			
		return qna_id;
	}
	
	//Qna 수정
	@Override
	public void updateQna(Map qnaMap) throws Exception {
		boardQnaDAO.updateQna(qnaMap);
		
	}

	//QNA 삭제
	@Override
	public void removeQna(int qna_id) throws Exception {
		boardQnaDAO.deleteQna(qna_id);
	}


	

}