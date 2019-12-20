package com.kratos.game.herphone.systemNotice.bean;

import lombok.Data;

@Data
public class ResSystemNoticeInfo {
	private String content;
	private long creatTime;
	private int type;
	private String noticeId;
	private int readState;
}
