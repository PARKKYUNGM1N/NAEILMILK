package com.project.naeil.admin.notice.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.project.naeil.admin.notice.vo.NoticeVO;

public interface AdminNoticeDAO {
	
	// 페이징
	public List selectAllNoticeList(Map pagingMap) throws DataAccessException;
	
	public int selectTotalNotice() throws DataAccessException;
	
	// 공지 작성
	public int insertNotice(Map newNoticeMap) throws DataAccessException;
	
	// 공지 상세
	public NoticeVO viewNotice(int notice_id) throws DataAccessException;
	
	// 공지 수정
	public void updateNotice(Map noticeMap) throws DataAccessException;
	
	// 삭제
	public void deleteNotice(int notice_id) throws DataAccessException;
	
	// 조회수
	public void updateCnt(int notice_no) throws DataAccessException;
	
}
