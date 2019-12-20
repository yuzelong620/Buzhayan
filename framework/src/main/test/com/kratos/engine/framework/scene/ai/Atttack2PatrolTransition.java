package com.kratos.engine.framework.scene.ai;

import com.kratos.engine.framework.scene.BattleArea;
import com.kratos.engine.framework.scene.actor.Creature;
import com.kratos.engine.framework.scene.actor.Player;

public class Atttack2PatrolTransition extends Transition {

	public Atttack2PatrolTransition(State from, State to) {
		super(from, to);
	}

	@Override
	public boolean meetCondition(Creature creature) {
		Player player = (Player)creature;
		BattleArea scene = (BattleArea) player.getScene();

		return scene.getAllMonsters().stream().allMatch(Creature::isDie);
	}

}
