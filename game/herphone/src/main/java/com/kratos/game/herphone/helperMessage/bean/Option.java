package com.kratos.game.herphone.helperMessage.bean;

import lombok.Data;

@Data
public class Option {

	private Integer answerId;
	private String text;
	
	public Option() {
	}

	public Option(Integer answerId, String text) {
		this.answerId = answerId;
		this.text = text;
	}
}
