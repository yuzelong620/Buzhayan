package com.kratos.game.herphone.intimacy.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
@Data
@Document
public class NoteEntity {
	public NoteEntity() {
	
	}
	public NoteEntity(String id,int num) {
		this.id=id;
		this.num=num;
	}
	@Id
	private String id;
	@Indexed
	private int num;
}
