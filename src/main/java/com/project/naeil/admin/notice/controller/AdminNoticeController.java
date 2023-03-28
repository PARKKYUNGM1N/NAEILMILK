package com.project.naeil.admin.notice.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public interface AdminNoticeController {
	
	// 조회
	public ModelAndView listNotice(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	// 공지 작성
	public ResponseEntity insertNotice(MultipartHttpServletRequest multipartRequest, HttpServletResponse response) throws Exception;
	
	// 공지 상세
	public ModelAndView viewNotice(@RequestParam("notice_id") int notice_id, HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	// 공지 수정
	public ResponseEntity updateNotice(HttpServletRequest multipartRequest, HttpServletResponse response) throws Exception;
	
	// 삭제
	public void deleteNotice(@RequestParam("notice_id") int notice_id, HttpServletRequest request, HttpServletResponse response) throws Exception;
	
}
