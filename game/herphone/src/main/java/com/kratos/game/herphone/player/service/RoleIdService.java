package com.kratos.game.herphone.player.service;


import com.kratos.engine.framework.crud.ICrudService;
import com.kratos.game.herphone.player.domain.RoleId;

public interface RoleIdService extends ICrudService<String, RoleId> {
    String getNextRoleId();
}
