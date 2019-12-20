package com.kratos.engine.framework.tencent;


import com.kratos.engine.framework.tencent.bean.ResTencentUserInfo;

public interface TencentService {
    ResTencentUserInfo getUserInfo(String accessToken, String openid);
}
