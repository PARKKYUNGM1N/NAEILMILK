package com.project.naeil.admin.notice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.project.naeil.admin.notice.dao.AdminNoticeDAO;
import com.project.naeil.admin.notice.vo.NoticeVO;

@Service("adminNoticeService")
@Transactional(propagation=Propagation.REQUIRED)
public class AdminNoticeServiceImpl implements AdminNoticeService {
	@Autowired
	private AdminNoticeDAO adminNoticeDAO;
	
	// 페이징
	@Override
	public Map listNotice(Map pagingMap) throws Exception {
		Map noticeMap = new HashMap();
		List<NoticeVO> noticeList = adminNoticeDAO.selectAllNoticeList(pagingMap);
		
		// 모든 글 수
		int totalNotice = adminNoticeDAO.selectTotalNotice();
		
		noticeMap.put("noticeList", noticeList);
		noticeMap.put("totalNotice", totalNotice);
		
		return noticeMap;
	}
	
	// 공지 작성
	@Override
	public int insertNotice(Map newNoticeMap) throws Exception {
		int notice_id = adminNoticeDAO.insertNotice(newNoticeMap);
		
		return notice_id;
	}
	
	// 공지 상세
	@Override
	public Map viewNotice(int notice_id) throws Exception {
		
		Map noticeMap = new HashMap();
		
		NoticeVO noticeVO = adminNoticeDAO.viewNotice(notice_id);
		
		noticeMap.put("noticeVO", noticeVO);
		
		
		return noticeMap;
	}
	
	// 공지 수정
	@Override
	public void updateNotice(Map noticeMap) throws Exception {
		adminNoticeDAO.updateNotice(noticeMap);
	}
	
	// 삭제
	@Override
	public void deleteNotice(int notice_id) throws Exception {
		adminNoticeDAO.deleteNotice(notice_id);
	}
	
}
