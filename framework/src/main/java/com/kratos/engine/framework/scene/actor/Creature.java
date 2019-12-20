package com.kratos.engine.framework.scene.actor;

import lombok.Getter;
import lombok.Setter;

/**
 * 生物体，就是会动的场景演员
 * @author herton
 */
@Getter
@Setter
public abstract class Creature extends SceneActor {
	
	protected long hp;
	
	protected int attack;

	public void changeHp(long changeHp) {
		this.hp += changeHp;
	}

	public boolean isDie() {
		return this.hp <= 0;
	}
}
