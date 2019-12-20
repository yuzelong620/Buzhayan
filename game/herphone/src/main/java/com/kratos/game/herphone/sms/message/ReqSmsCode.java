package com.kratos.game.herphone.sms.message;

import lombok.Data;

@Data
public class ReqSmsCode {
	private String mobile;
	private String code;
}
