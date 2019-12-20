package com.kratos.game.herphone.dynamic.bean;

import lombok.Data;
@Deprecated
/**
 * 资源bean
 */
@Data
public class ResBean {	
	int type;//1文字，2图片，3视频
	String content;
	String coverPicture;//視頻封面

	public ResBean(int type, String content) {
		this.type = type;
		this.content = content;
	}
	public ResBean(int type, String content,String coverPicture) {
		this.type = type;
		this.content = content;
		this.coverPicture=coverPicture;
	}
	public ResBean() {
	}

}
