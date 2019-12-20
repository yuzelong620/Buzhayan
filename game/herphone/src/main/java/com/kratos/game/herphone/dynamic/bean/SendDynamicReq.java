package com.kratos.game.herphone.dynamic.bean;

import java.util.List;

import lombok.Data;
/**
 * 发送
 */
@Deprecated
@Data
public class SendDynamicReq {
	List<ResBean> resList;//资源列表
	List<Integer> tags;//标签分类
}
