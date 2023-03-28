package com.project.naeil.admin.notice.service;

import java.util.Map;

public interface AdminNoticeService {
	
	// 페이징
	public Map listNotice(Map pagingMap) throws Exception;
	
	// 공지 작성
	public int insertNotice(Map newNoticeMap) throws Exception;
	
	// 공지 상세
	public Map viewNotice(int notice_id) throws Exception;
	
	// 공지 수정
	public void updateNotice(Map noticeMap) throws Exception;
	
	// 삭제
	public void deleteNotice(int notice_id) throws Exception;
	
}
