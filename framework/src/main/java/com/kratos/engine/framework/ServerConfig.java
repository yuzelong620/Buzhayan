package com.kratos.engine.framework;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kratos.config")
public class ServerConfig {
    /**
     * 服务器id
     */
    public int serverId;
    public int gamePort;
}
