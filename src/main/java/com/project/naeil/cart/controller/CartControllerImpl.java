package com.project.naeil.cart.controller;

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

import com.project.naeil.cart.service.CartService;
import com.project.naeil.cart.vo.CartVO;
import com.project.naeil.common.base.BaseController;
import com.project.naeil.member.vo.MemberVO;

@Controller("cartController")
@RequestMapping(value="/cart")
public class CartControllerImpl extends BaseController implements CartController{
	@Autowired
	private CartService cartService;
	@Autowired
	private CartVO cartVO;
	@Autowired
	private MemberVO memberVO;
	
	@RequestMapping(value="/myCartList.do" ,method = RequestMethod.GET)
	public ModelAndView myCartMain(HttpServletRequest request, HttpServletResponse response)  throws Exception {
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		HttpSession session=request.getSession();
		MemberVO memberVO=(MemberVO)session.getAttribute("memberInfo");
		String member_id=memberVO.getMember_id();
		cartVO.setMember_id(member_id);
		
		// 장바구니 페이지에 표시할 상품 정보를 조회합니다.
		Map<String ,List> cartMap = cartService.myCartList(cartVO);
		
		// 장바구니 목록을 세션에 저장합니다.
		session.setAttribute("cartMap", cartMap);//장바구니 목록 화면에서 상품 주문 시 사용하기 위해서 장바구니 목록을 세션에 저장한다.
		//mav.addObject("cartMap", cartMap);
		return mav;
	}
	@RequestMapping(value="/addGoodsInCart.do", method = RequestMethod.POST, produces = "application/text; charset=utf8")
												// 전송된 상품 번호를 받습니다.
	public @ResponseBody String addGoodsInCart(@RequestParam("goods_id") int goods_id, 
											   @RequestParam("order_goods_qty") int order_goods_qty,
			                    			   HttpServletRequest request, HttpServletResponse response)  throws Exception{
		HttpSession session=request.getSession();
		memberVO=(MemberVO)session.getAttribute("memberInfo");
		String member_id=memberVO.getMember_id();
		
		cartVO.setMember_id(member_id);
		//카트 등록전에 이미 등록된 제품인지 판별한다.
		cartVO.setGoods_id(goods_id);
		cartVO.setMember_id(member_id);
		cartVO.setCart_goods_qty(order_goods_qty);
		// 상품 번호가 장바구니 테이블에 있는지 조회합니다.
		boolean isAreadyExisted = cartService.findCartGoods(cartVO);
		System.out.println("isAreadyExisted:" + isAreadyExisted);
		
		// 상품 번호가 이미 장바구니 테이블에 있으면 이미 추가되었다는 메시지를 브라우저로 전송하고, 없으면 장바구니 테이블에 추가합니다.
		if(isAreadyExisted==true){
			return "already_existed";
		}else{
			cartService.addGoodsInCart(cartVO);
			return "add_success";
		}
	}
	
	@RequestMapping(value="/modifyCartQty.do" ,method = RequestMethod.POST)
	public @ResponseBody String modifyCartQty(@RequestParam("goods_id") int goods_id,
			                                  @RequestParam("cart_goods_qty") int cart_goods_qty,
			                                  HttpServletRequest request, HttpServletResponse response) throws Exception{
		HttpSession session=request.getSession();
		memberVO=(MemberVO)session.getAttribute("memberInfo");
		String member_id=memberVO.getMember_id();
		cartVO.setGoods_id(goods_id);
		cartVO.setMember_id(member_id);
		cartVO.setCart_goods_qty(cart_goods_qty);
		boolean result=cartService.modifyCartQty(cartVO);
		
		if(result==true){
			return "modify_success";
		}else{
			return "modify_failed";	
		}
		
	}
	
	@RequestMapping(value="/removeCartGoods.do" ,method = RequestMethod.POST)
	public ModelAndView removeCartGoods(@RequestParam("cart_id") int cart_id,
			                            HttpServletRequest request, HttpServletResponse response)  throws Exception{
		ModelAndView mav=new ModelAndView();
		cartService.removeCartGoods(cart_id);
		mav.setViewName("redirect:/cart/myCartList.do");
		return mav;
	}
	
	// 장바구니 선택 삭제
	@RequestMapping(value = "/checkRemove.do", method = RequestMethod.POST)
	public ModelAndView checkRemove(@RequestParam("checkedCartIds") String checkedCartIds,
									HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		System.out.println(checkedCartIds);
		String[] checkedCart = checkedCartIds.split(",");
		
		for (String cart_Id : checkedCart) {
			System.out.println(cart_Id);
			cartService.removeCartGoods(Integer.parseInt(cart_Id));

		}
		
		mav.setViewName("redirect:/cart/myCartList.do");
		return mav;
	}
	
}
