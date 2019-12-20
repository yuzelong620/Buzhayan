package com.kratos.game.herphone.systemNotice.bean;

import java.util.Set;
import lombok.Data;

@Data
public class ChangeReadStateReq {
	Set<String> systemNoticeIds;
}
