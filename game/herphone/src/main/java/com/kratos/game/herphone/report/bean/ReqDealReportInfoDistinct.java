package com.kratos.game.herphone.report.bean;

import lombok.Data;

@Data
public class ReqDealReportInfoDistinct {
	private String discussid;
	private int type;
	private int opinion;	//0不处理1处理
}
