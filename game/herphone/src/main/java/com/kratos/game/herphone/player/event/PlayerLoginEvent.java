package com.kratos.game.herphone.player.event;

import com.kratos.engine.framework.event.BaseEvent;
import com.kratos.game.herphone.player.domain.Player;

public class PlayerLoginEvent implements BaseEvent {
	
	private Player player;
	
	public PlayerLoginEvent(Player player) {
		this.player = player;
	}
	
	@Override
	public Player getOwner() {
		return player;
	}

}
