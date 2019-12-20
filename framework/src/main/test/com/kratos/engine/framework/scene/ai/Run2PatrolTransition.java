package com.kratos.engine.framework.scene.ai;

import com.kratos.engine.framework.scene.actor.Creature;

public class Run2PatrolTransition extends Transition {

	public Run2PatrolTransition(State from, State to) {
		super(from, to);
	}

	@Override
	public boolean meetCondition(Creature creature) {
		// 逃跑后肯定是巡逻啦
		return true;
	}

}
