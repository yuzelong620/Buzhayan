package com.kratos.game.herphone.dynamic.bean;

import java.util.List;

import lombok.Data;
@Data
@Deprecated
public class DynamicReplyReq {
	List<ResBean> resList;//资源列表
	String toDynamicId;
}
