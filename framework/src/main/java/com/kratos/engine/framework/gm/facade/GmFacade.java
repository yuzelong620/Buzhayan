package com.kratos.engine.framework.gm.facade;

import com.kratos.engine.framework.gm.GmCommands;
import com.kratos.engine.framework.gm.GmDispatcher;
import com.kratos.engine.framework.gm.GmHandler;
import com.kratos.engine.framework.gm.message.ReqGmCommand;
import com.kratos.engine.framework.net.socket.IoSession;
import com.kratos.engine.framework.net.socket.annotation.MessageHandler;
import com.kratos.engine.framework.net.socket.task.DefaultMessageDispatcher;
import com.kratos.engine.framework.scheme.support.BaseConfigResourceRegistry;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j
@Component
public class GmFacade {

    private GmDispatcher gmDispatcher = GmDispatcher.getInstance();
//    @Autowired
//    private BaseConfigResourceRegistry configResourceRegistry;

//    @MessageHandler
//    public void reqGmExec(IoSession session, ReqGmCommand req) {
//        long playerId = session.getPlayerId();
//        String[] params = req.getParams().split("\\s+");
//        gmDispatcher.dispatch(playerId, params);
//    }

//    @GmHandler(cmd = GmCommands.CONFIG)
//    public void gmSetConfig(long playerId, String cmd, String file) {
//        if("reload".equals(cmd)) {
//            try {
//                configResourceRegistry.getConfig(Class.forName(file)).reload();
//            } catch (ClassNotFoundException e) {
//                log.error("", e);
//            }
//        }
//    }
}
