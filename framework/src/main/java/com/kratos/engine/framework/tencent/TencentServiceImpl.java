package com.kratos.engine.framework.tencent;

import com.alibaba.fastjson.JSON;
import com.github.kevinsawicki.http.HttpRequest;
import com.kratos.engine.framework.tencent.bean.ResTencentUserInfo;

import java.util.HashMap;
import java.util.Map;

public class TencentServiceImpl implements TencentService {
    private final String APP_ID;
    private final String SECRET;

    public static final String GET_USER_INFO = "https://graph.qq.com/user/get_user_info";

    private TencentServiceImpl(String appId, String secret) {
        this.APP_ID = appId;
        this.SECRET = secret;
    }

    public static TencentService of(String appId, String secret) {
       return new TencentServiceImpl(appId, secret);
    }

    @Override
    public ResTencentUserInfo getUserInfo(String accessToken, String openid) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("openid", openid);
        params.put("oauth_consumer_key", APP_ID);
        String result = HttpRequest.get(GET_USER_INFO, params, true).accept("application/json").body();

        return JSON.parseObject(result, ResTencentUserInfo.class);
    }
}
