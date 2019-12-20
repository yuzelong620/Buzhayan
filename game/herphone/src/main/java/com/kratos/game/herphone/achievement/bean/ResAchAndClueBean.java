package com.kratos.game.herphone.achievement.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
/**返回类 获得的成就集合和线索集合*/
public class ResAchAndClueBean {
	List<Integer> clueList = new ArrayList<Integer>();
	Set<Integer> achlist = new HashSet<>();
	
	public ResAchAndClueBean(List<Integer> clueList, Set<Integer> achlist) {	
		this.clueList = clueList;
		this.achlist = achlist;
	}

	public ResAchAndClueBean() {
		
	}		
}
