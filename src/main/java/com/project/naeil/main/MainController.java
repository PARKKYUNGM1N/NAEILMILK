package com.project.naeil.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.project.naeil.common.base.BaseController;
import com.project.naeil.goods.service.GoodsService;
import com.project.naeil.goods.vo.GoodsVO;

@Controller("mainController")
@EnableAspectJAutoProxy
public class MainController extends BaseController {
	@Autowired
	private GoodsService goodsService;

	@RequestMapping(value= "/main/main.do", method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HttpSession session;
		ModelAndView mav=new ModelAndView();
		String viewName=(String)request.getAttribute("viewName");
		mav.setViewName(viewName);
		
		session=request.getSession();
		
		// 속성, 즉 side_menu의 값에 따라 화면 왼쪽에 표시되는 메뉴 항목을 다르게 합니다.
		session.setAttribute("side_menu", "user");
		
		// 베스트셀러, 신간, 스테디셀러 정보를 조회해 Map에 저장합니다.
		Map<String,List<GoodsVO>> goodsMap=goodsService.listGoods();
		
		// goods_id를 토대로 리뷰 평균 별점을 가져오는 메서드
		Map<String,List<GoodsVO>> AvgStarMap=goodsService.selectGoodsAvgStar();
		
		// goodsMap의 list<GoodsVO>>의 GoodsVO에 goods_avg_star값을 AvgStarMap의  GoodsVO에 goods_avg_star값을 대입시키는 조건문
		if (AvgStarMap.containsKey("bestgoods") && goodsMap.containsKey("bestgoods")) {
		    List<GoodsVO> bestGoodsList = goodsMap.get("bestgoods");
		    List<GoodsVO> avgStarGoodsList = AvgStarMap.get("bestgoods");
		    int size = Math.min(bestGoodsList.size(), avgStarGoodsList.size());
		    for (int i = 0; i < size; i++) {
		        GoodsVO avgStarGoodsVO = avgStarGoodsList.get(i);
		        for (GoodsVO goodsVO : bestGoodsList) {
		            if (goodsVO.getGoods_id() == avgStarGoodsVO.getGoods_id()) {
		                goodsVO.setGoods_avg_star(avgStarGoodsVO.getGoods_avg_star());
		                break;
		            }
		        }
		    }
		    goodsMap.put("bestgoods", bestGoodsList);
		}
		
		if (AvgStarMap.containsKey("newgoods") && goodsMap.containsKey("newgoods")) {
		    List<GoodsVO> bestGoodsList = goodsMap.get("newgoods");
		    List<GoodsVO> avgStarGoodsList = AvgStarMap.get("newgoods");
		    int size = Math.min(bestGoodsList.size(), avgStarGoodsList.size());
		    for (int i = 0; i < size; i++) {
		        GoodsVO avgStarGoodsVO = avgStarGoodsList.get(i);
		        for (GoodsVO goodsVO : bestGoodsList) {
		            if (goodsVO.getGoods_id() == avgStarGoodsVO.getGoods_id()) {
		                goodsVO.setGoods_avg_star(avgStarGoodsVO.getGoods_avg_star());
		                break;
		            }
		        }
		    }
		    goodsMap.put("newgoods", bestGoodsList);
		}
		
		if (AvgStarMap.containsKey("on_sale") && goodsMap.containsKey("on_sale")) {
		    List<GoodsVO> bestGoodsList = goodsMap.get("on_sale");
		    List<GoodsVO> avgStarGoodsList = AvgStarMap.get("on_sale");
		    int size = Math.min(bestGoodsList.size(), avgStarGoodsList.size());
		    for (int i = 0; i < size; i++) {
		        GoodsVO avgStarGoodsVO = avgStarGoodsList.get(i);
		        for (GoodsVO goodsVO : bestGoodsList) {
		            if (goodsVO.getGoods_id() == avgStarGoodsVO.getGoods_id()) {
		                goodsVO.setGoods_avg_star(avgStarGoodsVO.getGoods_avg_star());
		                break;
		            }
		        }
		    }
		    goodsMap.put("on_sale", bestGoodsList);
		}
		
		
		// 메인 페이지로 상품 정보를 전달합니다.
		mav.addObject("goodsMap", goodsMap);
		
		
		
		return mav;
	}
}