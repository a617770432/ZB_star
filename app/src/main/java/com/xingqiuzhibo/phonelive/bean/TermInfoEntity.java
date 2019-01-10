package com.xingqiuzhibo.phonelive.bean;

import java.util.Date;
import java.util.List;

/**
 * 社区动态发布内容表
 * 
 * @author dh
 * @email 123456@qq.com
 * @date 2019-01-04 10:43:42
 */
public class TermInfoEntity {

	//帖子Id
	private Integer termId;

	/**
	 * 发表者id
	 */
	private Long uid;

	private String uname;
	/**
	 * 是否收费 0免费  1收费
	 */
	private Integer isFree;
	/**
	 * 需收取的钻石数作为费用
	 */
	private Integer amount;
	/**
	 * post创建日期，永久不变，一般不显示给用户
	 */
	private String createTime;
	/**
	 * post内容
	 */
	private String content;
	/**
	 * post标题
	 */
	private String title;
	/**
	 * 视频地址
	 */
	private String video;
	/**
	 * 内容类型  0文本内容，1图片内容，2视频内容
	 */
	private Integer filetype;
	/**
	 * 类型  0社区动态，1圈子动态
	 */
	private Integer type;
	/**
	 * 状态，0待审核  1已审核，2未审核通过
	 */
	private Integer auditStatus;
	/**
	 * 使用状态，1有效，0失效
	 */
	private Integer status;
	/**
	 * 栏目ID
	 */
	private Long oneRid;
	/**
	 * 栏目ID
	 */
	private Long twoRid;
	/**
	 * 栏目名称
	 */
	private String oneRangeName;
	/**
	 * 栏目名称
	 */
	private String twoRangeName;
	/**
	 * 评论数
	 */
	private Long commentCount;
	/**
	 * 浏览数
	 */
	private Integer hits;
	/**
	 * 赞数
	 */
	private Integer assist;
	/**
	 * 置顶 1置顶； 0不置顶
	 */
	private Integer istop;
	/**
	 * 推荐 1推荐 0不推荐
	 */
	private Integer recommended;
	/**
	 * 排序序号
	 */
	private Integer sort;

//	@ApiModelProperty("审核原因")
	private String auditReason;

//	@ApiModelProperty("图片集")
	private List<String> imgList;

//	@ApiModelProperty("是否点过赞 0未点赞 1已点赞")
	private Integer isstar;

//	@ApiModelProperty("是否关注人 0未关注 1已关注")
	private Integer isfocus;

	public Integer getTermId() {
		return termId;
	}

	public void setTermId(Integer termId) {
		this.termId = termId;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public Integer getIsFree() {
		return isFree;
	}

	public void setIsFree(Integer isFree) {
		this.isFree = isFree;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public Integer getFiletype() {
		return filetype;
	}

	public void setFiletype(Integer filetype) {
		this.filetype = filetype;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getOneRid() {
		return oneRid;
	}

	public void setOneRid(Long oneRid) {
		this.oneRid = oneRid;
	}

	public Long getTwoRid() {
		return twoRid;
	}

	public void setTwoRid(Long twoRid) {
		this.twoRid = twoRid;
	}

	public String getOneRangeName() {
		return oneRangeName;
	}

	public void setOneRangeName(String oneRangeName) {
		this.oneRangeName = oneRangeName;
	}

	public String getTwoRangeName() {
		return twoRangeName;
	}

	public void setTwoRangeName(String twoRangeName) {
		this.twoRangeName = twoRangeName;
	}

	public Long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	public Integer getAssist() {
		return assist;
	}

	public void setAssist(Integer assist) {
		this.assist = assist;
	}

	public Integer getIstop() {
		return istop;
	}

	public void setIstop(Integer istop) {
		this.istop = istop;
	}

	public Integer getRecommended() {
		return recommended;
	}

	public void setRecommended(Integer recommended) {
		this.recommended = recommended;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getAuditReason() {
		return auditReason;
	}

	public void setAuditReason(String auditReason) {
		this.auditReason = auditReason;
	}

	public List<String> getImgList() {
		return imgList;
	}

	public void setImgList(List<String> imgList) {
		this.imgList = imgList;
	}

	public Integer getIsstar() {
		return isstar;
	}

	public void setIsstar(Integer isstar) {
		this.isstar = isstar;
	}

	public Integer getIsfocus() {
		return isfocus;
	}

	public void setIsfocus(Integer isfocus) {
		this.isfocus = isfocus;
	}
}
