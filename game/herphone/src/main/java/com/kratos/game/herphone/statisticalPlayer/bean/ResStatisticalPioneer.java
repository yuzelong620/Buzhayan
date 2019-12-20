package com.kratos.game.herphone.statisticalPlayer.bean;

import java.text.SimpleDateFormat;

import com.kratos.game.herphone.statisticalPlayer.entity.StatisticalPioneerEntity;

import lombok.Data;

@Data
public class ResStatisticalPioneer {
	private String id;	//唯一索引 （date_playerId)拼接
	private int date;
	private String playerId;
	private String roleId;
	private String creatTime;
	private String nickName;
	private String sex;			
	private int dealNum;		//治理次数
	private int succcessNum;	//成功治理次数
	private int isEyeshield;	//是否护眼大队
	
	public ResStatisticalPioneer(StatisticalPioneerEntity statisticalPioneerEntity) {
		super();
		this.id = statisticalPioneerEntity.getId();
		this.date = statisticalPioneerEntity.getDate();
		this.playerId = String.valueOf(statisticalPioneerEntity.getPlayerId());
		this.roleId = statisticalPioneerEntity.getRoleId();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.creatTime = simpleDateFormat.format(statisticalPioneerEntity.getCreatTime());
		this.nickName = statisticalPioneerEntity.getNickName();
		this.sex = statisticalPioneerEntity.getSex();
		this.dealNum = statisticalPioneerEntity.getDealNum();
		this.succcessNum = statisticalPioneerEntity.getSucccessNum();
		this.isEyeshield = statisticalPioneerEntity.getIsEyeshield();
	}

	public ResStatisticalPioneer() {
		super();
	}
	
	
	
}
