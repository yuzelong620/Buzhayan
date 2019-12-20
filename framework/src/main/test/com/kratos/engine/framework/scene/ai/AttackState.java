package com.kratos.engine.framework.scene.ai;

import com.kratos.engine.framework.scene.BattleArea;
import com.kratos.engine.framework.scene.actor.Creature;
import com.kratos.engine.framework.scene.actor.Player;

public class AttackState implements State {

	@Override
	public void onEnter(Creature creature) {
		// 进入攻击状态
		
	}

	@Override
	public void onExit(Creature creature) {
		// 离开攻击状态
	}

	@Override
	public void execute(Creature creature) {
		Player player = (Player)creature;
        BattleArea scene = (BattleArea) player.getScene();
		// 攻击第一个没死的怪物
		scene.getAllMonsters().stream().filter(monster -> !monster.isDie()).findFirst().ifPresent(monster -> {
			player.changeHp(-monster.getAttack());
			monster.changeHp(-player.getAttack());
			System.err.println(player.getName() + "邂逅敌人["+monster.getName()+"]，快使用双截棍，哼哼哈兮。"
					+ "我方血量["+ player.getHp() + "]"
					+ "敌方血量["+ monster.getHp() + "]");
		});
	}

}
