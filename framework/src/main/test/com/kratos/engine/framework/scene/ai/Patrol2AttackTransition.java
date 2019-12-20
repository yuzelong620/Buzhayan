package com.kratos.engine.framework.scene.ai;

import com.kratos.engine.framework.scene.BattleArea;
import com.kratos.engine.framework.scene.actor.Creature;
import com.kratos.engine.framework.scene.actor.Player;

public class Patrol2AttackTransition extends Transition {

	public Patrol2AttackTransition(State from, State to) {
		super(from, to);
	}

	@Override
	public boolean meetCondition(Creature creature) {
		// 如果当前在巡逻状态，且所有怪物没死，就揍它
		Player player = (Player)creature;
		BattleArea scene = (BattleArea) player.getScene();
		
		return !scene.getAllMonsters().stream().allMatch(Creature::isDie);
	}

}
