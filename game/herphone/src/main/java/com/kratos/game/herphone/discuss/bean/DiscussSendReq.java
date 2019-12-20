package com.kratos.game.herphone.discuss.bean;

import lombok.Data;

@Data
public class DiscussSendReq {
    int gameId=-1;
    int chatId=-1;
	int talkId;
	int optionIndex;
    String content="";
}
