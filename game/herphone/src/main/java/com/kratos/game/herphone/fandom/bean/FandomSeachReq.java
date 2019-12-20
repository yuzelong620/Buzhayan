package com.kratos.game.herphone.fandom.bean;

import lombok.Data;
//查找game的粉絲圈
@Data
@Deprecated
public class FandomSeachReq {
	int gameId;
	int page;
}
