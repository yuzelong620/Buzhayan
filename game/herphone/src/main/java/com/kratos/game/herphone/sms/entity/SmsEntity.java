package com.kratos.game.herphone.sms.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document
public class SmsEntity{
	
	public SmsEntity() {
		 
	}
	public SmsEntity(String id, String code, long validTime){
		this.id = id;
		this.code = code;
		this.validTime = validTime;
	}
	
	@Id
	private String id;//id 手机区号+手机号  中国：86
	private String code;//
	private long validTime;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public long getValidTime() {
		return validTime;
	}
	public void setValidTime(long validTime) {
		this.validTime = validTime;
	}
	
	

}
