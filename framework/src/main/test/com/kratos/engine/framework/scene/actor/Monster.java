package com.kratos.engine.framework.scene.actor;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Monster extends Creature  {
    private String name;

	public Monster(long hp, int attack) {
		setHp(hp);
		setAttack(attack);
        setType(ActorType.Monster);
	}

	@Override
	public String toString() {
		return "Monster [hp=" + hp + ", attack=" + attack + "]";
	}
}
