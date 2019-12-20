package com.kratos.game.herphone.config;

import com.kratos.engine.framework.tencent.TencentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class TencentConfiguration {
    public static final String APP_ID = "1109515925";
    public static final String SECRET = "HUqWbj3irfpTv4IJ";

    @Autowired
    private TencentManager tencentManager;

    @PostConstruct
    public void config() {
        tencentManager.create(APP_ID, SECRET);
    }
}
