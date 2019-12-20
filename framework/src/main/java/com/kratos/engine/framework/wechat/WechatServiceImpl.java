package com.kratos.engine.framework.wechat;

import com.alibaba.fastjson.JSON;
import com.github.kevinsawicki.http.HttpRequest;
import com.kratos.engine.framework.wechat.bean.ResAccessToken;
import com.kratos.engine.framework.wechat.bean.ResWechatUserInfo;

import java.util.HashMap;
import java.util.Map;

public class WechatServiceImpl implements WechatService {
    private final String APP_ID;
    private final String SECRET;

    public static final String GET_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/jscode2session";
    public static final String GET_APP_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    public static final String GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo";

    private WechatServiceImpl(String appId, String secret) {
        this.APP_ID = appId;
        this.SECRET = secret;
    }

    public static WechatService of(String appId, String secret) {
       return new WechatServiceImpl(appId, secret);
    }

    @Override
    public ResAccessToken getAccessToken(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("appid", APP_ID);
        params.put("secret", SECRET);
        params.put("grant_type", "authorization_code");
        params.put("js_code", code);
        String result = HttpRequest.get(GET_ACCESS_TOKEN, params, true).accept("application/json").body();

        return JSON.parseObject(result, ResAccessToken.class);
    }

    @Override
    public ResAccessToken getAppAccessToken(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("appid", APP_ID);
        params.put("secret", SECRET);
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        String result = HttpRequest.get(GET_APP_ACCESS_TOKEN, params, true).accept("application/json").body();

        return JSON.parseObject(result, ResAccessToken.class);
    }

    @Override
    public ResWechatUserInfo getUserInfo(String accessToken, String openid) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("openid", openid);
        String result = HttpRequest.get(GET_USER_INFO, params, true).accept("application/json").body();

        return JSON.parseObject(result, ResWechatUserInfo.class);
    }
}
