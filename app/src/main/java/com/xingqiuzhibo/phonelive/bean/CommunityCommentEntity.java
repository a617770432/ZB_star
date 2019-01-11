package com.xingqiuzhibo.phonelive.bean;

import java.util.Date;

/**
 * 社区评论表
 * 
 * @author dh
 * @email 123456@qq.com
 * @date 2019-01-04 10:43:42
 */
public class CommunityCommentEntity {
	/**
	 * 
	 */
	private Long cid;

	/**
	 * 评论内容类型 1动态内容，2圈子内容
	 */
	private String type;
	/**
	 * 评论内容 id
	 */
	private Long targetId;
	/**
	 * 发表评论的用户id
	 */
	private Long uid;
	/**
	 * 被评论的用户id
	 */
	private Integer toUid;
	/**
	 * 评论者昵称
	 */
	private String fullName;
	/**
	 * 评论时间
	 */
	private String createTime;
	/**
	 * 评论内容
	 */
	private String content;
	/**
	 * 被回复的评论id
	 */
	private Long parentid;
	/**
	 * 状态，0待审核 1已审核，2未审核通过
	 */
	private Integer status;

//	@ApiModelProperty("赞数")
	private Integer assist;

//	@TableField(exist = false)
//	@ApiModelProperty("是否点赞 0未点赞 1已点赞")
	private Integer isstar;


	public Integer getAssist() {
		return assist;
	}

	public void setAssist(Integer assist) {
		this.assist = assist;
	}

	public Integer getIsstar() {
		return isstar;
	}

	public void setIsstar(Integer isstar) {
		this.isstar = isstar;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * 设置：
	 */
	public void setCid(Long cid) {
		this.cid = cid;
	}
	/**
	 * 获取：
	 */
	public Long getCid() {
		return cid;
	}
	/**
	 * 设置：评论内容类型 1动态内容，2圈子内容
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：评论内容类型 1动态内容，2圈子内容
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：评论内容 id
	 */
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
	/**
	 * 获取：评论内容 id
	 */
	public Long getTargetId() {
		return targetId;
	}
	/**
	 * 设置：发表评论的用户id
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}
	/**
	 * 获取：发表评论的用户id
	 */
	public Long getUid() {
		return uid;
	}
	/**
	 * 设置：被评论的用户id
	 */
	public void setToUid(Integer toUid) {
		this.toUid = toUid;
	}
	/**
	 * 获取：被评论的用户id
	 */
	public Integer getToUid() {
		return toUid;
	}
	/**
	 * 设置：评论者昵称
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * 获取：评论者昵称
	 */
	public String getFullName() {
		return fullName;
	}
	/**
	 * 设置：评论内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：评论内容
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置：被回复的评论id
	 */
	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}
	/**
	 * 获取：被回复的评论id
	 */
	public Long getParentid() {
		return parentid;
	}
	/**
	 * 设置：状态，0待审核 1已审核，2未审核通过
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态，0待审核 1已审核，2未审核通过
	 */
	public Integer getStatus() {
		return status;
	}
}
