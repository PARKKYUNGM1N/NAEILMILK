package com.project.naeil.goods.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.project.naeil.board.qna.service.BoardQnaService;
import com.project.naeil.board.review.service.BoardReviewService;
import com.project.naeil.common.base.BaseController;
import com.project.naeil.goods.service.GoodsService;
import com.project.naeil.goods.vo.GoodsVO;

import net.sf.json.JSONObject;

@Controller("goodsController")
@RequestMapping(value="/goods")
public class GoodsControllerImpl extends BaseController implements GoodsController {
	@Autowired
	private GoodsService goodsService;
	
	// 리뷰글을 불러오기 위한 의존성 추가
	@Autowired
	private BoardReviewService boardReviewService;
	
	@Autowired
	private BoardQnaService boardQnaService;
	
	@RequestMapping(value="/goodsDetail.do", method={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView goodsDetail(@RequestParam(value="goods_id", required=false) String goods_id, HttpServletRequest request, HttpSession session) throws Exception {
	    String viewName = (String)request.getAttribute("viewName");
	    Map goodsMap = null;
	    
	    // goods_id 값이 없을 경우 세션에서 조회
	    if (goods_id == null) {
	        goods_id = (String)session.getAttribute("goods_id");
	    }
	    
	    if (goods_id != null) {
	        goodsMap = goodsService.goodsDetail(goods_id);
	    }
	    
	    ModelAndView mav = new ModelAndView(viewName);
	    mav.addObject("goodsMap", goodsMap);

	    if (goodsMap != null) {
	        // 조회한 상품 정보를 빠른 메뉴에 표시하기 위해 전달합니다.
	        GoodsVO goodsVO = (GoodsVO) goodsMap.get("goodsVO");
	        addGoodsInQuick(goods_id, goodsVO, session);

	        //리뷰글을 갖고오는 메서드
	        session.setAttribute("goodsInfo", goodsVO);
	        List reviewList = boardReviewService.listReview(goods_id);
	        mav.addObject("reviewList", reviewList);

	        //qna 갖고오는 메서드
	        session.setAttribute("goodsInfo", goodsVO);
	        List qnaList = boardQnaService.listQna(goods_id);
	        mav.addObject("qnaList", qnaList);
	    }

	    return mav;
	}
																		  // 브라우저로 전송하는 JSON 데이터의 한글 인코딩을 지정합니다.
	@RequestMapping(value="/keywordSearch.do",method = RequestMethod.GET, produces = "application/text; charset=utf8")
	// @ResponseBody => JSON 데이터를 브라우저로 출력합니다. // @RequestParam("keyword") String keyword => 검색할 키워드를 가져옵니다.
	public @ResponseBody String  keywordSearch(@RequestParam("keyword") String keyword,
			                                  HttpServletRequest request, HttpServletResponse response) throws Exception{
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//System.out.println(keyword);
		if(keyword == null || keyword.equals(""))
		   return null ;
	
		keyword = keyword.toUpperCase();
		
		// 가져온 키워드가 포함된 상품 제목을 조회합니다.
	    List<String> keywordList = goodsService.keywordSearch(keyword);
	    
	    // 최종 완성될 JSONObject 선언(전체)
		JSONObject jsonObject = new JSONObject();
		
		// 조회한 데이터를 JSON에 저장합니다.
		jsonObject.put("keyword", keywordList);
		
		// JSON을 문자열로 변환한 후 브라우저로 출력합니다.
	    String jsonInfo = jsonObject.toString();
	   // System.out.println(jsonInfo);
	    return jsonInfo ;
	}
	
	@RequestMapping(value="/searchGoods.do" ,method = RequestMethod.GET)
	public ModelAndView searchGoods(@RequestParam("searchWord") String searchWord,
			                       HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName=(String)request.getAttribute("viewName");
		
		// 검색창에서 가져온 단어가 포함된 상품 제목을 조회합니다.
		List<GoodsVO> goodsList=goodsService.searchGoods(searchWord);
		
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("goodsList", goodsList);
		return mav;
		
	}
	
	private void addGoodsInQuick(String goods_id,GoodsVO goodsVO,HttpSession session){
		boolean already_existed=false;
		List<GoodsVO> quickGoodsList; //최근 본 상품 저장 ArrayList
		
		// 세션에 저장된 최근 본 상품 목록을 가져옵니다.
		quickGoodsList=(ArrayList<GoodsVO>)session.getAttribute("quickGoodsList");
		
		// 최근 본 상품이 있는 경우
		if(quickGoodsList!=null){
			// 상품 목록이 네 개 이하인 경우
			if(quickGoodsList.size() < 4){ //미리본 상품 리스트에 상품개수가 세개 이하인 경우
				// 상품 목록을 가져와 이미 존재하는 상품인지 비교합니다. 이미 존재할 경우 already_existed를 true로 설정합니다.
				for(int i=0; i<quickGoodsList.size();i++){
					GoodsVO _goodsBean=(GoodsVO)quickGoodsList.get(i);
					if(goods_id.equals(_goodsBean.getGoods_id())){
						already_existed=true;
						break;
					}
				}
				
				// already_existed가 false이면 상품 정보를 목록에 저장합니다.
				if(already_existed==false){
					quickGoodsList.add(goodsVO);
				}
				
			}
			
		}else{
			quickGoodsList =new ArrayList<GoodsVO>();
			quickGoodsList.add(goodsVO);
			
		}
		// 최근 본 상품 목록을 세션에 저장합니다.
		session.setAttribute("quickGoodsList",quickGoodsList);
		
		// 최근 본 상품 목록에 저장된 상품 개수를 세션에 저장합니다.
		session.setAttribute("quickGoodsListNum", quickGoodsList.size());
	}
}