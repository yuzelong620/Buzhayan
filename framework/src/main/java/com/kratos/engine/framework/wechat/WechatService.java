package com.kratos.engine.framework.wechat;

import com.kratos.engine.framework.wechat.bean.ResAccessToken;
import com.kratos.engine.framework.wechat.bean.ResWechatUserInfo;

public interface WechatService {
    ResAccessToken getAccessToken(String code);
    ResAccessToken getAppAccessToken(String code);
    ResWechatUserInfo getUserInfo(String accessToken, String openid);
}
