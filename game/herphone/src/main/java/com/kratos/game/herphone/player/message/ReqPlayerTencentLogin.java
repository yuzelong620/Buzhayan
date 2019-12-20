package com.kratos.game.herphone.player.message;

import lombok.Data;

@Data
public class ReqPlayerTencentLogin {
	String channelId="";
	String openId;
	String accessToken;
	String unionid;//qq unionid
}
