package com.kratos.game.herphone.badge.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ResBadgeInfo {
	private int state;
	private List<ResBadgeBean> list = new ArrayList<ResBadgeBean>();
}
