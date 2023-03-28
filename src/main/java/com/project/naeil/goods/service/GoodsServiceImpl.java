package com.project.naeil.goods.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.project.naeil.goods.dao.GoodsDAO;
import com.project.naeil.goods.vo.GoodsVO;
import com.project.naeil.goods.vo.ImageFileVO;

@Service("goodsService")
@Transactional(propagation=Propagation.REQUIRED)
public class GoodsServiceImpl implements GoodsService{
	@Autowired
	private GoodsDAO goodsDAO;
	
	public Map<String,List<GoodsVO>> listGoods() throws Exception {
		Map<String,List<GoodsVO>> goodsMap=new HashMap<String,List<GoodsVO>>();
		
		// newbook, bestseller, steadyseller를 조건으로 각각 도서 정보를 조회해서 HashMap에 저장한 후 반환합니다.
		List<GoodsVO> goodsList=goodsDAO.selectGoodsList("bestgoods");
		goodsMap.put("bestgoods",goodsList);
		
		goodsList=goodsDAO.selectGoodsList("newgoods");
		goodsMap.put("newgoods",goodsList);
		
		goodsList=goodsDAO.selectGoodsList("on_sale");
		goodsMap.put("on_sale",goodsList);
		
		return goodsMap;
		//
	}
	
	public Map goodsDetail(String goods_id) throws Exception {
		Map goodsMap=new HashMap();
		
		// 상품 정보와 이미지 정보를 조회한 후 HashMap에 저장합니다.
		GoodsVO goodsVO = goodsDAO.selectGoodsDetail(goods_id);
		goodsMap.put("goodsVO", goodsVO);
		List<ImageFileVO> imageList =goodsDAO.selectGoodsDetailImage(goods_id);
		goodsMap.put("imageList", imageList);
		
		return goodsMap;
	}
	
	public List<String> keywordSearch(String keyword) throws Exception {
		List<String> list=goodsDAO.selectKeywordSearch(keyword);
		return list;
	}
	
	public List<GoodsVO> searchGoods(String searchWord) throws Exception{
		List goodsList=goodsDAO.selectGoodsBySearchWord(searchWord);
		return goodsList;
	}

	@Override
	public Map<String,List<GoodsVO>> selectGoodsAvgStar() throws Exception {
		Map<String,List<GoodsVO>> AvgStarMap= new HashMap<String,List<GoodsVO>>();
		
		List<GoodsVO> AvgStar=goodsDAO.selectGoodsAvgStar("bestgoods");
		AvgStarMap.put("bestgoods", AvgStar);
		
		AvgStar=goodsDAO.selectGoodsAvgStar("newgoods");
		AvgStarMap.put("newgoods", AvgStar);
		
		AvgStar=goodsDAO.selectGoodsAvgStar("on_sale");
		AvgStarMap.put("on_sale", AvgStar);
		
		
		
		return AvgStarMap;
	}
	
	
}