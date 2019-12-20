package com.kratos.engine.framework.scene.ai;

import com.kratos.engine.framework.common.utils.IdGenerator;
import com.kratos.engine.framework.scene.BattleArea;
import com.kratos.engine.framework.scene.actor.Monster;
import com.kratos.engine.framework.scene.actor.Player;
import org.junit.Test;

public class AiTest {

	@Test
	public void fightTest() throws InterruptedException {
		Player player = new Player(100, 15);
        player.setId(IdGenerator.getNextTempId());
        player.setName("陈浩南");
        Player player2 = new Player(100, 15);
        player2.setId(IdGenerator.getNextTempId());
        player2.setName("山鸡");
		Monster monster = new Monster(120, 10);
        monster.setId(IdGenerator.getNextTempId());
        monster.setName("靓坤");
        Monster monster2 = new Monster(120, 10);
        monster2.setId(IdGenerator.getNextTempId());
        monster2.setName("B哥");
        BattleArea scene = new BattleArea();
		scene.setId(IdGenerator.getNextTempId());
		player.setScene(scene);
        player2.setScene(scene);
		monster.setScene(scene);
        monster2.setScene(scene);
		
		State patrolState = new PatrolState();
		State attackState = new AttackState();
		State runState = new RunAwayState();
		
		Transition transition1 = new Patrol2AttackTransition(patrolState, attackState);
		Transition transition2 = new Attack2RunTransition(attackState, runState);
		Transition transition3 = new Atttack2PatrolTransition(attackState, patrolState);
		Transition transition4 = new Run2PatrolTransition(runState, patrolState);
		
		FiniteStateMachine fsm = new FiniteStateMachine();
		fsm.setInitState(patrolState);
		
		fsm.addTransition(transition1);
		fsm.addTransition(transition2);
		fsm.addTransition(transition3);
		fsm.addTransition(transition4);

        FiniteStateMachine fsm2 = new FiniteStateMachine();
        fsm2.setInitState(patrolState);
        fsm2.addTransition(transition1);
        fsm2.addTransition(transition2);
        fsm2.addTransition(transition3);
        fsm2.addTransition(transition4);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                    System.err.println("开始冻结");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                fsm.freeze(3000);
//            }
//        }).start();
//		while (true) {
//			fsm.enterFrame(player);
//            //fsm2.enterFrame(player2);
//			Thread.sleep(500);
//		}
	}

}
