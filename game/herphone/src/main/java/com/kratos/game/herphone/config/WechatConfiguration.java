package com.kratos.game.herphone.config;

import com.kratos.engine.framework.wechat.WechatManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class WechatConfiguration {
    public static final String WX_APP_ID = "wxc518351b9a4e2d33";
    public static final String WX_SECRET = "59545bbc5c5c04afa9217197075a1a88";

    @Autowired
    private WechatManager wechatManager;

    @PostConstruct
    public void config() {
        wechatManager.create(WX_APP_ID, WX_SECRET);
    }
}
