package com.kratos.engine.framework.scene.ai;

import com.kratos.engine.framework.scene.actor.Creature;
import com.kratos.engine.framework.scene.actor.Player;

public class PatrolState implements State {

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
		System.err.println(player.getName() + "大王叫我来寻山，寻完南山寻北山");
	}

}
