package com.kratos.engine.framework.wechat.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResAccessToken {
    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String errcode;
    private String errmsg;
    private String unionid;
}
