package com.kratos.game.herphone.common.bean;

import lombok.Data;

@Data
public class PlayerExtraBean {
	
	int extraPowerLimit;// 额外增加的体力上限
	double extraRecoverRote;// 额外恢复 体力加成。 小数
	
	public PlayerExtraBean() { 
	}
	public PlayerExtraBean(int extraPowerLimit, double extraRecoverRote) { 
		this.extraPowerLimit = extraPowerLimit;
		this.extraRecoverRote = extraRecoverRote;
	}
}
