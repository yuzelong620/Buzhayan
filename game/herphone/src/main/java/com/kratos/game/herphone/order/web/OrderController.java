package com.kratos.game.herphone.order.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;
import com.kratos.game.herphone.order.bean.ReqOrderBean;

@RestController
@RequestMapping("/order")
@PrePermissions
public class OrderController extends BaseController{
	/**
	 * 购买商品
	 */
	@PostMapping("/buyItem")
	@PrePermissions
	  public ResponseEntity<?> buyItem(@RequestBody ReqOrderBean reqOrderBean) {
		orderService.buyItem(reqOrderBean);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
