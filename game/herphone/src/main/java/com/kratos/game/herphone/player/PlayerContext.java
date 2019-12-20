package com.kratos.game.herphone.player;

import com.kratos.game.herphone.player.domain.Player;
import org.springframework.util.Assert;

/**
 * 可以从当前线程中获取会员信息
 *
 * @author herton
 */
public class PlayerContext {
    private static ThreadLocal<Player> contextHolder = new ThreadLocal<>();

    public static Player getPlayer() {
        return contextHolder.get();
    }

    public static void setPlayer(Player player) {
        Assert.notNull(player, "会员不能为空");
        contextHolder.set(player);
    }

    public static void clear() {
        contextHolder.remove();
    }
}