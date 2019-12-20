package com.kratos.game.herphone.blackList.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class BlackListEntity {
	@Id
	private long playerId;
	private List<Long> blackList = new ArrayList<Long>();

}
