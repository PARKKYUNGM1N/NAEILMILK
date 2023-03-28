package com.project.naeil.admin.notice.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.project.naeil.admin.notice.vo.NoticeVO;

@Repository("adminNoticeDAO")
public class AdminNoticeDAOImpl implements AdminNoticeDAO {
	@Autowired
	private SqlSession sqlSession;
	
	// 전체 조회
	@Override
	public List selectAllNoticeList(Map pagingMap) throws DataAccessException {
		List<NoticeVO> noticeList = sqlSession.selectList("mapper.admin.notice.selectAllNotice", pagingMap);
		
		return noticeList;
	}
	
	// 페이징에 필요한 전체 개수
	@Override
	public int selectTotalNotice() throws DataAccessException {
		int totalNotice = sqlSession.selectOne("mapper.admin.notice.selectTotalNotice");
		
		return totalNotice;
	}
	
	// 공지 작성
	@Override
	public int insertNotice(Map newNoticeMap) throws DataAccessException {
		sqlSession.insert("mapper.admin.notice.insertNotice", newNoticeMap);
		
		return Integer.parseInt((String) newNoticeMap.get("notice_id"));
	}
	
	// 공지 조회
	@Override
	public NoticeVO viewNotice(int notice_id) throws DataAccessException {
		updateCnt(notice_id);

		return sqlSession.selectOne("mapper.admin.notice.viewNotice", notice_id);
	}
	
	// 수정
	@Override
	public void updateNotice(Map noticeMap) throws DataAccessException {
		sqlSession.update("mapper.admin.notice.updateNotice", noticeMap);
	}
	
	// 삭제
	@Override
	public void deleteNotice(int notice_id) throws DataAccessException {
		sqlSession.delete("mapper.admin.notice.deleteNotice", notice_id);
	}
	
	// 조회수
	@Override
	public void updateCnt(int notice_id) throws DataAccessException {
		sqlSession.update("mapper.admin.notice.updateCnt", notice_id);
	}
	
}
