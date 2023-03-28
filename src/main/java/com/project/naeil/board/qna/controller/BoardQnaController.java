package com.project.naeil.board.qna.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

public interface BoardQnaController {

	//리뷰 전체조회(페이징)
	public ModelAndView listQna(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	//QNA 상세
	public ModelAndView viewQna(@RequestParam("qna_id") int notice_id, HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	//QNA 작성
	public ResponseEntity addNewQna(HttpServletRequest request, HttpServletResponse response) throws Exception;
		
	// 공지 수정
	public ResponseEntity updateNotice(HttpServletRequest multipartRequest, HttpServletResponse response) throws Exception;
	
	//QNA 삭제
	public void removeQna(HttpServletRequest request, HttpServletResponse response,
							@RequestParam(value = "qna_id", required = false) int qna_id) throws Exception;
}