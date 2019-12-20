package com.kratos.game.herphone.gamemanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.globalgame.auto.json.Admin_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.AdminCache;
import com.kratos.game.herphone.util.StringUtil;

@Service
public class AdminUserService{
		/**后台管理用户登录*/
		
		public boolean adminLogin(String userName,String password) {
			if (StringUtil.isNullAndEmpty(userName)||StringUtil.isNullAndEmpty(password)) {
				throw new BusinessException("用户名密码不能为空");
			}
			List<Admin_Json> jsons = JsonCacheManager.getCache(AdminCache.class).getList();
			for (int i = 0; i < jsons.size(); i++) {
				if (jsons.get(i).getUserName().equals(userName)&&jsons.get(i).getPassWord().equals(password)) {
					return true;
				}
			}
			return false;
		}
		
}
