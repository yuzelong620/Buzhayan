package com.kratos.game.herphone.pioneer.bean;

import lombok.Data;

@Data
public class ResPioneerBean {
	private String playerId;
	private String roleId;
	private long creatTime;
	private String nickName;
	private String sex;			
	private int dealNum;		//治理次数
	private int succcessNum;	//成功治理次数
	private int isEyeshield;	//是否护眼大队
	
}
