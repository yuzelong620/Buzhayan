package com.kratos.game.herphone.discuss.bean;

import lombok.Data;

@Data
public class DiscussListByPageReq {
    int gameId=-1;
    int chatId=-1;
	int talkId;
	int optionIndex;
    int page=-1;
}
