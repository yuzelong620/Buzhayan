package com.kratos.game.herphone.discuss.bean;
 

import java.util.ArrayList;
import java.util.List;

import com.kratos.game.herphone.discuss.entity.DiscussEntity;
import com.kratos.game.herphone.message.bean.DiscussBean;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;

import lombok.Data;
 
@Data
public class TopDiscussBean extends DiscussBean{   
	public TopDiscussBean(DiscussEntity obj) {
		super(obj);
	}
	public TopDiscussBean(DiscussEntity obj,PlayerDynamicEntity playerDynamicEntity) {	
		this(obj);
		this.titleIds=playerDynamicEntity.getItemTitle();
		this.AchievementTags = playerDynamicEntity.getAchievementTags();
		this.avatarFrame = playerDynamicEntity.getAvatarFrame();
	}
	List<Integer> titleIds=new ArrayList<Integer>();
	int AchievementTags;//佩戴的成就徽章
	int avatarFrame;	//佩戴的头像框
}
