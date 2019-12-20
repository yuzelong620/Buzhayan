package com.kratos.game.herphone.player.message;

import lombok.Data;

@Data
public class ReqPlayerEdit {
	private String name;
	private String avatarUrl;
	private String gender;
	private String constellation;
}
