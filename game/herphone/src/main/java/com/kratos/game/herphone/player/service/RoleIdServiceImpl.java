package com.kratos.game.herphone.player.service;

import com.kratos.engine.framework.common.utils.JedisUtils;
import com.kratos.engine.framework.common.utils.StringHelper;
import com.kratos.engine.framework.crud.BaseCrudService;
import com.kratos.game.herphone.player.domain.RoleId;
import io.netty.util.internal.ConcurrentSet;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

@Log4j
@Component
public class RoleIdServiceImpl extends BaseCrudService<String, RoleId> implements RoleIdService {
    private volatile Set<String> roleIds = new ConcurrentSet<>();

    @PostConstruct
    public void init() {
        super.init();
//        List<RoleId> roleIds = findAll();
//
//        roleIds.forEach(roleId -> this.addToCache(roleId.getId()));
    }

    @Override
    public String getNextRoleId() {
        String roleId = String.valueOf(RandomUtils.nextInt(100000000, 999999999));
        if (StringHelper.isNotBlank(JedisUtils.getInstance().hGet("roleId", roleId))) {
            log.error("生成roleId重复：" + roleId);
            return getNextRoleId();
        }
        this.addToCache(roleId);
        cacheAndPersist(roleId, new RoleId(roleId));
        return roleId;
    }

    private void addToCache(String roleId) {
        JedisUtils.getInstance().hSet("roleId", roleId, 1);
    }
}
