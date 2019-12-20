package com.kratos.game.herphone.intimacy.bean;

import com.kratos.game.herphone.intimacy.entity.IntimacyEntity;

/**
 * 亲密度
 *
 */
public class IntimacyBean {

	public IntimacyBean() {

	}

	public IntimacyBean(IntimacyEntity entity) {
		this.toPlayerId = entity.getToPlayerId()+"";
		this.intimacyValue = entity.getIntimacyValue();
	}

	private String toPlayerId;
	private int intimacyValue;// 亲密度值

	public String getToPlayerId() {
		return toPlayerId;
	}

	public void setToPlayerId(String toPlayerId) {
		this.toPlayerId = toPlayerId;
	}

	public int getIntimacyValue() {
		return intimacyValue;
	}

	public void setIntimacyValue(int intimacyValue) {
		this.intimacyValue = intimacyValue;
	}

}
