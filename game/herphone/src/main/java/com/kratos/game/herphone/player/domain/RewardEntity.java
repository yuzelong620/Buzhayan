package com.kratos.game.herphone.player.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.kratos.game.herphone.bag.bean.ItemBean;

import lombok.Data;


/**
 * 奖励礼包激活码实体
 *
 * @author herton
 */
@Data
@Document
public class RewardEntity  {	

	@Id
    private String code = "";
    private long groupId = 0;
	private int state = 1;//领取状态 0未领取，1已领取
	private String timeStamp = "";
	private List<ItemBean> list ;
	
}
