package com.kratos.game.herphone.game.chose.message;

import lombok.Data;

@Data
public class ReqPlayerChose {
	private int gameId;
	private int chatId;
	private int talkId;
	private int optionIndex;
}
