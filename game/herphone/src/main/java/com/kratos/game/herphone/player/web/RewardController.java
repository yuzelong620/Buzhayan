package com.kratos.game.herphone.player.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.bag.bean.ItemBean;
import com.kratos.game.herphone.player.domain.RewardEntity;
import com.kratos.game.herphone.player.service.RewardService;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController
@RequestMapping("/reward")
@PrePermissions
public class RewardController {
	
	  @Autowired
	  private RewardService RewardService;
	  
	  /**
	     * 生成激活码
	     */
	    @GetMapping("/generateReward")
	    public ResponseEntity<List<RewardEntity>> generateReward(String num, String list,HttpServletRequest request ) {
	    	String ip = getIpAddress(request);
	    	log.info("请求地址："+ip);
	    	int n = Integer.valueOf(num);
	    	List<ItemBean> list1 = JSONArray.parseArray(list, ItemBean.class);
	      return new ResponseEntity<List<RewardEntity>>(RewardService.generateReward(n, list1), HttpStatus.OK);
	    }
	    
	    /**
	     * 兑换激活码
	     */
	    @PostMapping("/exchangeReward/{code}")
	    @PrePermissions
	    public ResponseEntity<List<ItemBean>> exchangeCode(@PathVariable String code) {
	      return new ResponseEntity<List<ItemBean>>(RewardService.exchangeCode(code), HttpStatus.OK);
	    }
	    /**
	     * 从request中获取请求方IP
	     * @param request
	     * @return
	     */
	    @GetMapping("/getip")
	    public  String getIpAddress(HttpServletRequest request) {  
	           String ip = request.getHeader("x-forwarded-for");  
	           if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	               ip = request.getHeader("Proxy-Client-IP");  
	            }  
	            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	               ip = request.getHeader("WL-Proxy-Client-IP");  
	            }  
	            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	                ip = request.getHeader("HTTP_CLIENT_IP");  
	            }  
	            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	               ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
	            }  
	            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	                ip = request.getRemoteAddr();  
	            }  
	            return ip;  
	        }
}
