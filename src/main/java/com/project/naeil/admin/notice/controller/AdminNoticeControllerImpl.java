package com.project.naeil.admin.notice.controller;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.project.naeil.admin.notice.service.AdminNoticeService;
import com.project.naeil.admin.notice.vo.NoticeVO;
import com.project.naeil.common.base.BaseController;
import com.project.naeil.goods.vo.ImageFileVO;
import com.project.naeil.member.vo.MemberVO;

@Controller("adminNoticeController")
@RequestMapping(value = "/admin/notice")
public class AdminNoticeControllerImpl extends BaseController implements AdminNoticeController {
	private static final String CURR_IMAGE_REPO_PATH1 = "C:\\shopping\\file_repo\\notice";
	
	@Autowired
	private AdminNoticeService adminNoticeService;
	
	@Autowired
	private NoticeVO noticeVO;
	
	// 페이징
	@Override
	@RequestMapping(value = "/listNotice.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView listNotice(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    String sectionParam = request.getParameter("section");
	    String pageNumParam = request.getParameter("pageNum");

	    int section = sectionParam != null ? Integer.parseInt(sectionParam) : 1;
	    int pageNum = pageNumParam != null ? Integer.parseInt(pageNumParam) : 1;

	    Map<String, Object> pagingMap = new HashMap<>();
	    pagingMap.put("section", section);
	    pagingMap.put("pageNum", pageNum);

	    Map<String, Object> noticeMap = adminNoticeService.listNotice(pagingMap);
	    noticeMap.put("section", section);
	    noticeMap.put("pageNum", pageNum);

	    String viewName = (String) request.getAttribute("viewName");
	    ModelAndView mav = new ModelAndView(viewName);
	    mav.addObject("noticeMap", noticeMap);

	    return mav;
	}
	
	// 공지 작성
	@Override
	@RequestMapping(value = "/insertNotice.do", method = RequestMethod.POST)
	public ResponseEntity insertNotice(MultipartHttpServletRequest multipartRequest, HttpServletResponse response) throws Exception {
		multipartRequest.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		
		// 글정보를 저장하기 위한 map
		Map newNoticeMap = new HashMap();
		Enumeration enu = multipartRequest.getParameterNames();
		
		// 글쓰기 창에서 전송된 글 정보 key/value
		while(enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = multipartRequest.getParameter(name);
			newNoticeMap.put(name, value);
		}
		
		// 첨부한 이미지 정보를 가져옵니다.
		String imageFileName = upload1(multipartRequest);
		
		HttpSession session = multipartRequest.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
		newNoticeMap.put("imageFileName", imageFileName);
		
		String message = null;
		ResponseEntity resEntity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		
		try {
			int notice_id = adminNoticeService.insertNotice(newNoticeMap);
			
			if (imageFileName != null && imageFileName.length() != 0) {
				File srcFile = new File(CURR_IMAGE_REPO_PATH1 + "\\" + "temp" + "\\" + imageFileName);
				File destDir = new File(CURR_IMAGE_REPO_PATH1 + "\\" + notice_id);
				FileUtils.moveFileToDirectory(srcFile, destDir, true);
			}
			
			message = "<script>";
			message += " alert('공지를 작성했습니다.');";
			message += " location.href='"+multipartRequest.getContextPath()+"/admin/notice/listNotice.do';";
			message += ("</script>");
			
			resEntity = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		} catch (Exception e) {
			
			File srcFile = new File(CURR_IMAGE_REPO_PATH1+"\\"+"temp"+"\\"+imageFileName);
			srcFile.delete();
			
			message = "<script>";
			message += " alert('오류가 발생했습니다. 다시 시도해 주세요');";
			message += " location.href='"+multipartRequest.getContextPath()+"/admin/notice/listNotice.do';";
			message += ("</script>");
			
			resEntity = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
			
		}
		
		
		return resEntity;
	}
	
	// 공지 상세
	@Override
	@RequestMapping(value = "/viewNotice.do", method = RequestMethod.GET)
	public ModelAndView viewNotice(@RequestParam("notice_id") int notice_id, 
								   HttpServletRequest request, HttpServletResponse response) throws Exception {

		String viewName = (String) request.getAttribute("viewName");
		HttpSession session = request.getSession();

		Map noticeMap = adminNoticeService.viewNotice(notice_id);

		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		mav.addObject("noticeMap", noticeMap);

		return mav;
	}
	
	// 공지 수정
	@Override
	@RequestMapping(value = "/updateNotice.do", method = RequestMethod.POST)
	public ResponseEntity updateNotice(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		Map<String, Object> noticeMap = new HashMap<String, Object>();
		Enumeration enu = request.getParameterNames();
		
		while(enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = request.getParameter(name);
			noticeMap.put(name, value);
		}
		
		String notice_id = (String) noticeMap.get("notice_id");
		String message;
		ResponseEntity resEntity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		
		try {
			adminNoticeService.updateNotice(noticeMap);
			
			message = "<script>";
			message += " alert('글을 수정했습니다.');";
			message += " location.href='" + request.getContextPath() + "/admin/notice/viewNotice.do?notice_id=" + notice_id + "';";
			message += " </script>";
			resEntity = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			
		} catch (Exception e) {
			message = "<script>";
			message += " alert('오류가 발생했습니다.다시 수정해주세요');";
			message += " location.href='" + request.getContextPath() + "/admin/notice/viewNotice.do?notice_id=" + notice_id + "';";
			message += " </script>";
			resEntity = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			
			e.printStackTrace();
		}
		
		return resEntity;
	}

	// 삭제
	@Override
	@RequestMapping(value = "/deleteNotice.do", method = RequestMethod.POST)
	public void deleteNotice(@RequestParam("notice_id") int notice_id, 
							 HttpServletRequest request, HttpServletResponse response) throws Exception {
		adminNoticeService.deleteNotice(notice_id);
	}

}
