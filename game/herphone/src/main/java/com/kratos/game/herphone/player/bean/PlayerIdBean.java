package com.kratos.game.herphone.player.bean;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class PlayerIdBean {
	
	private String playerid;//playerid
	private String roleid;//角色id
    private String phone;//手机号
    private int is_block;//封号
    private long no_speak_time;//禁言

	


	public PlayerIdBean() {
		
	}



	public PlayerIdBean(String playerid, String roleid, String phone, int is_block, long no_speak_time) {

		this.playerid = playerid;
		this.roleid = roleid;
		this.phone = phone;
		this.is_block = is_block;
		this.no_speak_time = no_speak_time;
	}





}
