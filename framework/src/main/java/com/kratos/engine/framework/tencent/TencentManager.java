package com.kratos.engine.framework.tencent;

import org.junit.Assert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TencentManager {
    private static final Map<String, TencentService> tencentServiceMap = new HashMap<>();
    private static final String DEFAULT_SERVICE = "DEFAULT_SERVICE";

    public TencentService create(String appId, String secret) {
        return create(DEFAULT_SERVICE, appId, secret);
    }

    public TencentService create(String name, String appId, String secret) {
        Assert.assertFalse("duplicated tencent service name", tencentServiceMap.containsKey(name));
        TencentService wechatService = TencentServiceImpl.of(appId, secret);
        tencentServiceMap.put(name, wechatService);
        return wechatService;
    }

    public TencentService getTencentService() {
        return getTencentService(DEFAULT_SERVICE);
    }

    public TencentService getTencentService(String name) {
        return tencentServiceMap.get(name);
    }
}