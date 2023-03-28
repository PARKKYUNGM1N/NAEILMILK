package com.project.naeil.board.review.controller;

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

import com.project.naeil.board.review.service.BoardReviewService;
import com.project.naeil.member.vo.MemberVO;


@Controller("boardController")
@RequestMapping(value="/board")
public class BoardReviewControllerImpl implements BoardReviewController {
	@Autowired
	private BoardReviewService boardReviewService;
	
	
	// 리뷰 전체조회(페이징)
	@Override
	@RequestMapping(value = "review/listReview.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView listReview(HttpServletRequest request, HttpServletResponse response) throws Exception {
		   String sectionParam = request.getParameter("section");
		   String pageNumParam = request.getParameter("pageNum");

		   int section = sectionParam != null ? Integer.parseInt(sectionParam) : 1;
		   int pageNum = pageNumParam != null ? Integer.parseInt(pageNumParam) : 1;

		   Map<String, Object> pagingMap = new HashMap<>();
		   pagingMap.put("section", section);
		   pagingMap.put("pageNum", pageNum);

		   Map<String, Object> reviewMap = boardReviewService.listReview(pagingMap);
		   reviewMap.put("section", section);
		   reviewMap.put("pageNum", pageNum);

		   String viewName = (String) request.getAttribute("viewName");
		   ModelAndView mav = new ModelAndView(viewName);
		   mav.addObject("reviewMap", reviewMap);

		   return mav;
	}
	
	
	@Override
	@RequestMapping(value = "/review/addNewReview.do")
	@ResponseBody
	public ResponseEntity addNewReview(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setCharacterEncoding("utf-8");
		Map<String, Object> ReviewMap = new HashMap<String, Object>();
		Enumeration enu = request.getParameterNames();
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = request.getParameter(name);
			ReviewMap.put(name, value);
		}
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
		String member_id = memberVO.getMember_id();
		String goods_id = (String) session.getAttribute("goods_id");
		
		ReviewMap.put("member_id", member_id);
		ReviewMap.put("goods_id", goods_id);
		
		String message;
		ResponseEntity resEntity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		
		try {
			
		int articleNO = boardReviewService.addNewReview(ReviewMap);
		
		message = "<script>";
		message += " alert('새글을 추가했습니다.');";
		message += " location.href='" + request.getContextPath() + "/goods/goodsDetail.do?goods_id="+goods_id+"'; ";
		message += " </script>";
		resEntity = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			
		} catch (Exception e) {
		 message = " <script>";
		 message += " alert('글을 추가하는데 실패했습니다. 다시 입력해주세요');";
		 message += " location.href='" + request.getContextPath() + "/board/review/reviewForm.do'; ";
		 message += " </script>";
		 resEntity = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		 e.printStackTrace();
		}
		
		return resEntity;
	}
	
	// @/board/*Form.do 일괄 처리
	@RequestMapping(value = "*/*Form.do", method = { RequestMethod.POST, RequestMethod.GET })
	private ModelAndView form(@RequestParam(value = "goods_id", required = false) String goods_id,
							  HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		if (viewName.equals("/board/review/reviewForm")) {
			HttpSession session = request.getSession();
			if (goods_id != null) {
				session.setAttribute("goods_id", goods_id);
			}

		}

		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		
		return mav;
	}
	
	
	//리뷰삭제
	@Override
	@RequestMapping(value = "/review/removeReview.do", method = RequestMethod.POST)
	public void removeReview(HttpServletRequest request, HttpServletResponse response, int review_id)
			throws Exception {
		
		boardReviewService.removeReviewReview(review_id);
	}
	

	
}