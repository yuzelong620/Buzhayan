package com.kratos.engine.framework.scene.ai;

import com.kratos.engine.framework.scene.actor.Creature;
import com.kratos.engine.framework.scene.actor.Player;

public class RunAwayState implements State {

	@Override
	public void onEnter(Creature creature) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onExit(Creature creature) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(Creature creature) {
		Player player = (Player) creature;
		System.err.println(player.getName() + "三十六计，走为上计");
	}

}
