package com.kratos.game.herphone.player.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

/**
 * 用户奖励兑换列表
 *
 * @author herton
 */
@Data
@Document
public class ExchangeRewardEntity {

	@Id
	private String roleid = "";
	private List<Long> groupIds = new ArrayList<Long>();

}
