package com.kratos.game.herphone.systemMessgae.entity;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class PublicSystemMessageEntity {
	@Id
	private long playerId;
	private Set<String> systemMessageSet = new HashSet<>();
	private Set<String> systemNotice = new HashSet<>();
}
