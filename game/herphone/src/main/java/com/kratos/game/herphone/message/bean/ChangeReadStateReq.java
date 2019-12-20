package com.kratos.game.herphone.message.bean;

import java.util.Set;
import lombok.Data;

@Data
public class ChangeReadStateReq {
	Set<String> messageIds;
}
