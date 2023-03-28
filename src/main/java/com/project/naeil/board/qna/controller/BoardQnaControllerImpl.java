package com.project.naeil.board.qna.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.project.naeil.board.qna.service.BoardQnaService;
import com.project.naeil.goods.vo.GoodsVO;
import com.project.naeil.member.vo.MemberVO;

@Controller("boardQnaController")
@RequestMapping(value="/board/qna")
public class BoardQnaControllerImpl implements BoardQnaController{
	
	private static final String CURR_IMAGE_REPO_PATH = null; //파일 경로 설정해야됨
	//의존성
	@Autowired
	private BoardQnaService boardQnaService;
	
	// QNA 전체조회(페이징)
	@Override
	@RequestMapping(value = "/listQna.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView listQna(HttpServletRequest request, HttpServletResponse response) throws Exception {
			  String sectionParam = request.getParameter("section");
	 		  String pageNumParam = request.getParameter("pageNum");

			  int section = sectionParam != null ? Integer.parseInt(sectionParam) : 1;
			  int pageNum = pageNumParam != null ? Integer.parseInt(pageNumParam) : 1;

			  Map<String, Object> pagingMap = new HashMap<>();
			  pagingMap.put("section", section);
			  pagingMap.put("pageNum", pageNum);

			  Map<String, Object> qnaMap = boardQnaService.listQna(pagingMap);
			  qnaMap.put("section", section);
			  qnaMap.put("pageNum", pageNum);

			  String viewName = (String) request.getAttribute("viewName");
			  ModelAndView mav = new ModelAndView(viewName);
			  mav.addObject("qnaMap", qnaMap);

			  return mav;
	}
	
	//QNA 상세
	@Override
	@RequestMapping(value = "/viewQna.do", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView viewQna(@RequestParam("qna_id") int qna_id, 
								HttpServletRequest request, HttpServletResponse response) throws Exception {

		String viewName = (String) request.getAttribute("viewName");
		HttpSession session = request.getSession();
		
		Map qnaMap = boardQnaService.viewQna(qna_id);
		
		GoodsVO goodsvo = (GoodsVO) session.getAttribute("goodsInfo");
		int goods_id= goodsvo.getGoods_id();
		if(goodsvo != null) {
			session.setAttribute("goods_id", goods_id);
		}
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		mav.addObject("qnaMap", qnaMap);

		return mav;
	}
	
	
	//QNA 글추가
	@Override
	@RequestMapping(value = "/addNewQna.do")
	@ResponseBody
	public ResponseEntity addNewQna(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		Map<String, Object> qnaMap = new HashMap<String, Object>();
		Enumeration enu = request.getParameterNames();
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = request.getParameter(name);
			qnaMap.put(name, value);
		}
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
		String member_id = memberVO.getMember_id();
		String goods_id = (String) session.getAttribute("goods_id");
		
		qnaMap.put("member_id", member_id);
		qnaMap.put("goods_id", goods_id);
		
		String message;
		ResponseEntity resEntity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
			
		try {
				
		int qna_id = boardQnaService.addNewQna(qnaMap);
			
		message = "<script>";
		message += " alert('새글을 추가했습니다.');";
		message += " location.href='" + request.getContextPath() + "/goods/goodsDetail.do?goods_id="+goods_id+"'; ";
		message += " </script>";
		resEntity = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
				
		} catch (Exception e) {
		message = " <script>";
		message += " alert('글을 추가하는데 실패했습니다. 다시 입력해주세요');";
		message += " location.href='" + request.getContextPath() + "/board/qna/qnaForm.do'; ";
		message += " </script>";
		resEntity = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		e.printStackTrace();
		}
			
		return resEntity;
	}
	
	
	// @/board/*Form.do 일괄 처리
		@RequestMapping(value = "/*Form.do", method = { RequestMethod.POST, RequestMethod.GET })
		private ModelAndView form(@RequestParam(value = "goods_id", required = false) String goods_id,
								  HttpServletRequest request, HttpServletResponse response) throws Exception {
			String viewName = (String) request.getAttribute("viewName");
			if (viewName.equals("/board/qna/qnaForm")) {
				HttpSession session = request.getSession();
				if (goods_id != null) {
					session.setAttribute("goods_id", goods_id);
				}

			}

			ModelAndView mav = new ModelAndView();
			mav.setViewName(viewName);
			return mav;
		}
	
	//QNA 수정
	@Override
	@RequestMapping(value = "/updateQna.do", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public ResponseEntity updateNotice(HttpServletRequest multipartRequest, HttpServletResponse response)
	throws Exception {
		multipartRequest.setCharacterEncoding("utf-8");
		Map<String, Object> qnaMap = new HashMap<String, Object>();
		Enumeration enu = multipartRequest.getParameterNames();
		
		while(enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = multipartRequest.getParameter(name);
			qnaMap.put(name, value);
		}
		
		String qna_id = (String) qnaMap.get("qna_id");
		String message;
		ResponseEntity resEntity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		
		try {
			boardQnaService.updateQna(qnaMap);
			
			message = "<script>";
			message += " alert('글을 수정했습니다.');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/qna/viewQna.do?qna_id=" + qna_id + "';";
			message += " </script>";
			resEntity = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			
		} catch (Exception e) {
			message = "<script>";
			message += " alert('오류가 발생했습니다.다시 수정해주세요');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/qna/viewQna.do?qna_id=" + qna_id + "';";
			message += " </script>";
			resEntity = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			
			e.printStackTrace();
		}
		
		return resEntity;
	}
		
		
	//QNA 삭제
	@Override
	@RequestMapping(value = "/removeQna.do", method = RequestMethod.POST)
	public void removeQna(HttpServletRequest request, HttpServletResponse response, int qna_id) throws Exception {
		boardQnaService.removeQna(qna_id);
	}

	

}