package com.kratos.engine.framework.scene.ai;

import com.kratos.engine.framework.scene.actor.Creature;

import java.util.*;

public class FiniteStateMachine {

	private State initState;

	private State currState;
	/** 各种状态以及对应的转换规则 */
	private Map<State, List<Transition>> state2Transitions = new HashMap<>();


	private volatile boolean running = true;

	private long freezeTimeOut;

	public void addTransition(Transition transition) {
        List<Transition> transitions = state2Transitions.computeIfAbsent(transition.fromState(), k -> new ArrayList<>());
        transitions.add(transition);
	}

	public State getInitState() {
		return initState;
	}

	public void setInitState(State initState) {
		this.initState = initState;
	}

	public void enterFrame(Creature creature) {

		if (this.currState == null) {
			this.currState = this.initState;
			this.currState.onEnter(creature);
		}

		if (System.currentTimeMillis() > freezeTimeOut) {
			this.currState.execute(creature);
			List<Transition> transitions = state2Transitions.get(this.currState);
			for (Transition transition:transitions) {
				if (transition.meetCondition(creature)) {
					this.currState.onExit(creature);
					this.currState = transition.toState();
					this.currState.onEnter(creature);
				}
			}
		}
	}

	/**
	 * 暂停ai
	 * @param timeout
	 */
	public void freeze(long timeout) {
	    this.running = false;
		this.freezeTimeOut = System.currentTimeMillis() + timeout;
	}





}
