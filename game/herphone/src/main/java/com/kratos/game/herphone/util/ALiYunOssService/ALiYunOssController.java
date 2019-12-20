package com.kratos.game.herphone.util.ALiYunOssService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
@RestController
@RequestMapping("/aliyun")
public class ALiYunOssController {
	@Autowired
	private ALiYunOssService ALiYunOssService;
	/**
	 * 向阿里云sts服务器获取临时安全凭证
	 * @return
	 * @throws ClientException
	 */
	@GetMapping("/getOssToken")
	public ResponseEntity<?> getOssToken() {
		AssumeRoleResponse assumeRoleResponse = ALiYunOssService.assumeRole();
		if (assumeRoleResponse == null) {
			throw new BusinessException("ossToken获取失败");
		}
		return new ResponseEntity<AssumeRoleResponse>(assumeRoleResponse, HttpStatus.OK);
	}
}
