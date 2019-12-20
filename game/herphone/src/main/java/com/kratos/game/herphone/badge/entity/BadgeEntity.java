package com.kratos.game.herphone.badge.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class BadgeEntity {
	@Id
	private int id;
	private long unlockNum;
}
