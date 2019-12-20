package com.kratos.engine.framework.wechat;

import org.junit.Assert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WechatManager {
    private static final Map<String, WechatService> wechatServiceMap = new HashMap<>();
    private static final String DEFAULT_SERVICE = "DEFAULT_SERVICE";

    public WechatService create(String appId, String secret) {
        return create(DEFAULT_SERVICE, appId, secret);
    }

    public WechatService create(String name, String appId, String secret) {
        Assert.assertFalse("duplicated wechat service name", wechatServiceMap.containsKey(name));
        WechatService wechatService = WechatServiceImpl.of(appId, secret);
        wechatServiceMap.put(name, wechatService);
        return wechatService;
    }

    public WechatService getWechatService() {
        return getWechatService(DEFAULT_SERVICE);
    }

    public WechatService getWechatService(String name) {
        return wechatServiceMap.get(name);
    }
}