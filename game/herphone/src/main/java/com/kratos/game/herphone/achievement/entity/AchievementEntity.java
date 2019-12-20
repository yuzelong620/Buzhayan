package com.kratos.game.herphone.achievement.entity;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class AchievementEntity {
	@Id
	private long playerId;	
	private Set<Integer> achievementIds = new HashSet<>(); //获得的成就碎片
	private Set<Integer> badgeIds = new HashSet<>();	   //获得的小徽章
	private Set<Integer> tags =  new HashSet<>();	   //获得的成就徽章
}
