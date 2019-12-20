package com.kratos.engine.framework.scene;

import com.kratos.engine.framework.scene.actor.Creature;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class Scene {
    private long id;
    private Map<Long, Creature> creatureMap = new ConcurrentHashMap<>();

    public void addCreature(Creature creature) {
        this.creatureMap.put(creature.getId(), creature);
    }

    public void removeCreature(Creature creature) {
        this.creatureMap.remove(creature.getId());
    }


    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(!(obj instanceof Scene)) {
            return false;
        }
        return this.getId() == ((Scene) obj).getId();
    }
}
