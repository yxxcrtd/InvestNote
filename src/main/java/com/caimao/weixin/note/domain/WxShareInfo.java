package com.caimao.weixin.note.domain;

/**
 * 微信分享
 */
public class WxShareInfo {
	private String title;
	private String desc;
	private String link;
	private String imgUrl;
	private String type;
	private String friend;
	private String qqlink;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFriend() {
		return friend;
	}

	public void setFriend(String friend) {
		this.friend = friend;
	}

	public String getQqlink() {
		return qqlink;
	}

	public void setQqlink(String qqlink) {
		this.qqlink = qqlink;
	}

}
