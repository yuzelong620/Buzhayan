package com.kratos.engine.framework.scene.actor;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Player extends Creature {
    private String name;

    public Player(long hp, int attack) {
        setHp(hp);
        setAttack(attack);
        setType(ActorType.Player);
    }

    @Override
    public String toString() {
        return "Player [hp=" + hp + ", attack=" + attack + "]";
    }

}
