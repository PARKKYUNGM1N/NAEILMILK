package com.project.naeil.common.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.project.naeil.member.vo.MemberVO;
import com.project.naeil.order.vo.OrderVO;

@Service("mailService")
public class MailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Async
	public void sendMail(Map receiverMap, List<OrderVO> myOrderList, MemberVO orderer) {
		String to, subject, body = "";
		String mail_template;
		OrderVO orderVO = (OrderVO) myOrderList.get(0);
		
		to = "qkrkyungals@naver.com";
//		to = orderer.getEmail1() + "@" + orderer.getEmail2();
		subject = orderer.getMember_name() + " 님 내일우유 주문 완료 안내 메일입니다.";
		
		MimeMessage message = mailSender.createMimeMessage();
		
		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			mail_template = readHtmlFile();
			
			body = setOrderInfo(mail_template, orderVO);
			
			messageHelper.setSubject(subject);
			messageHelper.setTo(to);
			messageHelper.setFrom(to, orderer.getMember_name());
			messageHelper.setText(body, true);
			mailSender.send(message);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}////
	
	private String setOrderInfo(String mail_template, OrderVO orderVO) {
	    String result = mail_template;
	    
	    result = result.replaceAll("_order_id", Integer.toString(orderVO.getOrder_id()));
	    result = result.replaceAll("_goods_id", Integer.toString(orderVO.getGoods_id()));
	    result = result.replaceAll("_goods_fileName", orderVO.getGoods_fileName());
	    result = result.replaceAll("_goods_title", orderVO.getGoods_title());
	    
	    return result;
	}
	
	private String readHtmlFile() {
	    String mail_template = null;
	    BufferedReader br = null;
	    InputStreamReader isr = null;
	    FileInputStream fis = null;
	    File file = null;
	    String temp = null;
	    ClassPathResource resource = new ClassPathResource("template/mail_template.html");
	    
	    try {
	        file = resource.getFile();
	        isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
	        br = new BufferedReader(isr);
	        
	        StringBuffer sb = new StringBuffer();
	        while ((temp = br.readLine()) != null) {
	            sb.append(temp + "\n");
	        }
	        mail_template = sb.toString();
	        
	    } catch(Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (br != null) br.close();
	            if (isr != null) isr.close();
	            if (fis != null) fis.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    return mail_template;
	}
	
}
