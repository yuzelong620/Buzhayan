package com.kratos.game.herphone.report.bean;

import lombok.Data;

@Data
public class ReqReportBean {
	private String toPlayerId;
	private String content;
	private String discussId;
	private int type;
}
