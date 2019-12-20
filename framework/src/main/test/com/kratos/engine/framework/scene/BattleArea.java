package com.kratos.engine.framework.scene;

import com.kratos.engine.framework.scene.actor.ActorType;
import com.kratos.engine.framework.scene.actor.Monster;

import java.util.List;
import java.util.stream.Collectors;

public class BattleArea extends Scene {

    public List<Monster> getAllMonsters() {
        return this.getCreatureMap()
                .values()
                .stream()
                .filter(creature -> creature.getType() == ActorType.Monster)
                .map(creature -> (Monster) creature)
                .collect(Collectors.toList());
    }
}
