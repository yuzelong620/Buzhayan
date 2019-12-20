package com.kratos.engine.framework.scene.actor;

import com.kratos.engine.framework.scene.Scene;
import lombok.Getter;
import lombok.Setter;

/**
 * 场景里的各种演员
 * @author herton
 */
@Getter
@Setter
public abstract class SceneActor extends GameObject {

	private Scene scene;
	
	private ActorType type;

	public void setScene(Scene scene) {
		if(this.scene != null && !scene.equals(this.scene)) {
			this.scene.removeCreature((Creature) this);
		}
		scene.addCreature((Creature) this);
		this.scene = scene;
	}
}
