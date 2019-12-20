package com.kratos.game.herphone.config;

import com.kratos.engine.framework.net.ServerNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartListener implements ApplicationRunner {
    @Value("${kratos.heartbeatTick}")
    private Integer heartbeatTick;
    @Value("${kratos.gamePort}")
    private Integer gamePort;
    @Autowired
    @Qualifier("socketServer")
    private ServerNode socketServer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        socketServer.init(gamePort, heartbeatTick);
//        socketServer.start();
    }

}
