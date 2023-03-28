package com.project.naeil.order.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.project.naeil.common.base.BaseController;
import com.project.naeil.common.mail.MailService;
import com.project.naeil.goods.vo.GoodsVO;
import com.project.naeil.member.vo.MemberVO;
import com.project.naeil.order.service.OrderService;
import com.project.naeil.order.vo.OrderVO;

@Controller("orderController")
@RequestMapping(value="/order")
public class OrderControllerImpl extends BaseController implements OrderController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderVO orderVO;
	
	@Autowired
	MailService mailService;
	
	@RequestMapping(value="/orderEachGoods.do" ,method = RequestMethod.POST)
	public ModelAndView orderEachGoods(@ModelAttribute("orderVO") OrderVO _orderVO,
			                       HttpServletRequest request, HttpServletResponse response)  throws Exception{
		
		request.setCharacterEncoding("utf-8");
		HttpSession session=request.getSession();
		session=request.getSession();
		
		Boolean isLogOn=(Boolean)session.getAttribute("isLogOn");
		String action=(String)session.getAttribute("action");
		
		// 로그인을 하지 않았다면 먼저 로그인 후 주문을 처리하도록 주문 정보와 주문 페이지 요청 URL을 세션에 저장합니다.
		if(isLogOn==null || isLogOn==false){
			session.setAttribute("orderInfo", _orderVO);
			session.setAttribute("action", "/order/orderEachGoods.do");
			return new ModelAndView("redirect:/member/loginForm.do");
		
		}else{
			
			// 로그인 후 세션에서 주문 정보를 가져와 바로 주문창으로 이동합니다. 
			if(action!=null && action.equals("/order/orderEachGoods.do")){
				orderVO=(OrderVO)session.getAttribute("orderInfo");
				session.removeAttribute("action");

			 // 이미 로그인을 했다면 바로 주문을 처리합니다.
			 }else {
				 orderVO=_orderVO;
			 }
		 }
		
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		// 주문 정보를 저장할 주문 ArrayList를 생성합니다.
		List myOrderList=new ArrayList<OrderVO>();
		
		// 브라우저에서 전달한 주문 정보를 ArrayList에 저장합니다.
		myOrderList.add(orderVO);

		MemberVO memberInfo=(MemberVO)session.getAttribute("memberInfo");
		
		// 주문 정보와 주문자 정보를 세션에 바인딩한 후 주문창으로 전달합니다.
		session.setAttribute("myOrderList", myOrderList);
		session.setAttribute("orderer", memberInfo);
		
		return mav;
	}
	
	@RequestMapping(value="/orderAllCartGoods.do" ,method = RequestMethod.POST)
										  // 선택한 상품 수량을 배열로 받습니다.
	public ModelAndView orderAllCartGoods(@RequestParam("cart_goods_qty") String[] cart_goods_qty,
			                 			  HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		HttpSession session=request.getSession();
		
		// 미리 세션에 저장한 장바구니 상품 목록을 가져옵니다.
		Map cartMap=(Map)session.getAttribute("cartMap");
		List myOrderList=new ArrayList<OrderVO>();
		List<GoodsVO> myGoodsList=(List<GoodsVO>)cartMap.get("myGoodsList");
		
		MemberVO memberVO=(MemberVO)session.getAttribute("memberInfo");
		
		// 장바구니 상품 개수만큼 반복합니다.
		for(int i=0; i<cart_goods_qty.length;i++){
			// 문자열로 결합되어 전송된 상품 번호와 주문 수량을 split()메서드를 이용해 분리합니다.
			String[] cart_goods=cart_goods_qty[i].split(":");
			
			for(int j = 0; j< myGoodsList.size();j++) {
				
				// 장바구니 목록에서 차례로 GoodsVO를 가져옵니다.
				GoodsVO goodsVO = myGoodsList.get(j);
				
				// GoodsVO의 상품 번호를 가져옵니다.
				int goods_id = goodsVO.getGoods_id();
				
				// 전송된 상품 번호와 GoodsVO의 상품 번호가 같으면 주문하는 상품이므로 OrderVO 객체를 생성한 후 상품 정보를 OrderVO에 설정합니다. 그리고 다시 myOrderList에 저장합니다.
				if(goods_id==Integer.parseInt(cart_goods[0])) {
					OrderVO _orderVO=new OrderVO();
					String goods_title=goodsVO.getGoods_title();
					int goods_sales_price=goodsVO.getGoods_sales_price();
					String goods_fileName=goodsVO.getGoods_fileName();
					_orderVO.setGoods_id(goods_id);
					_orderVO.setGoods_title(goods_title);
					_orderVO.setGoods_sales_price(goods_sales_price);
					_orderVO.setGoods_fileName(goods_fileName);
					_orderVO.setOrder_goods_qty(Integer.parseInt(cart_goods[1]));
					myOrderList.add(_orderVO);
					break;
					
				}
			}
		}
		
		// 장바구니 목록에서 주문하기 위해 선택한 상품만 myOrderList에 저장한 후 세션에 바인딩합니다.
		session.setAttribute("myOrderList", myOrderList);
		
		session.setAttribute("orderer", memberVO);
		
		return mav;
	}	
	
	@RequestMapping(value="/payToOrderGoods.do" ,method = RequestMethod.POST)
										// 주문창에서 입력한 상품 수령자 정보와 배송지 정보를 Map에 바로 저장합니다.
	public ModelAndView payToOrderGoods(@RequestParam Map<String, String> receiverMap,
			                       		HttpServletRequest request, HttpServletResponse response)  throws Exception{
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		HttpSession session=request.getSession();
		MemberVO memberVO=(MemberVO)session.getAttribute("orderer");
		String member_id=memberVO.getMember_id();
		String orderer_name=memberVO.getMember_name();
		String orderer_hp = memberVO.getHp1()+"-"+memberVO.getHp2()+"-"+memberVO.getHp3();
		List<OrderVO> myOrderList=(List<OrderVO>)session.getAttribute("myOrderList");
		
		// 주문창에서 입력한 수령자 정보와 배송지 정보를 주문 상품 정보 목록과 합칩니다.
		for(int i=0; i<myOrderList.size();i++){
			OrderVO orderVO=(OrderVO)myOrderList.get(i);
			// 각 orderVO에 수령자 정보를 설정한 후 다시 myOrderList에 저장합니다.
			orderVO.setMember_id(member_id);
			orderVO.setOrderer_name(orderer_name);
			orderVO.setReceiver_name(receiverMap.get("receiver_name"));
			
			orderVO.setReceiver_hp1(receiverMap.get("receiver_hp1"));
			orderVO.setReceiver_hp2(receiverMap.get("receiver_hp2"));
			orderVO.setReceiver_hp3(receiverMap.get("receiver_hp3"));
			orderVO.setReceiver_tel1(receiverMap.get("receiver_tel1"));
			orderVO.setReceiver_tel2(receiverMap.get("receiver_tel2"));
			orderVO.setReceiver_tel3(receiverMap.get("receiver_tel3"));
			
			orderVO.setDelivery_address(receiverMap.get("delivery_address"));
			orderVO.setDelivery_message(receiverMap.get("delivery_message"));
			orderVO.setDelivery_method(receiverMap.get("delivery_method"));
			orderVO.setGift_wrapping(receiverMap.get("gift_wrapping"));
			orderVO.setPay_method(receiverMap.get("pay_method"));
			orderVO.setCard_com_name(receiverMap.get("card_com_name"));
			orderVO.setCard_pay_month(receiverMap.get("card_pay_month"));
			orderVO.setPay_orderer_hp_num(receiverMap.get("pay_orderer_hp_num"));	
			orderVO.setOrderer_hp(orderer_hp);	
			myOrderList.set(i, orderVO); //각 orderVO에 주문자 정보를 세팅한 후 다시 myOrderList에 저장한다.
		}//end for
		
		// 주문 정보를 SQL문으로 전달합니다.
	    orderService.addNewOrder(myOrderList);
	    
	    // 주문 완료 결과창에 주문자 정보를 표시하도록 전달합니다.
		mav.addObject("myOrderInfo",receiverMap);
		
		// 주문 완료 결과창에 주문 상품 목록을 표시하도록 전달합니다.
		mav.addObject("myOrderList", myOrderList);
		
		
		// 주문 결과 메일
		MemberVO orderer = (MemberVO) session.getAttribute("orderer");
		mailService.sendMail(receiverMap, myOrderList, orderer);
		
		return mav;
	}
	
}
