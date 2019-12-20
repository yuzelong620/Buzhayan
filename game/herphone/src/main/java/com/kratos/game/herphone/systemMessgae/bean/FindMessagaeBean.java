package com.kratos.game.herphone.systemMessgae.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FindMessagaeBean {
	private int page;
	private int count;
	private List<ResPlayerSystemMessagae> list = new ArrayList<ResPlayerSystemMessagae>();
}
