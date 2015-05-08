package com.vcredit.jdev.p2p.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


//用户推荐记录
@Entity
@Table(name="t_rec_record")
public class AccountRecommendRecord {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_rec_record_seq", unique = true, nullable = false)
	private Long accountRecommendSequence;// 流水号
	@Column(name = "t_acct_seq",nullable = false)
	private Long accountSequence;// 用户流水号
	@Column(name = "rec_frd_id",nullable = false,length=60)
	private String friendId;// 好友账号
	@Column(name = "rec_frd_type",nullable = false)
	private Integer friendIdType;// 好友账号类别 常量-参考数据字典
	@Column(name = "rec_frd_date",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date recommendDate;// 推荐时间
	@Column(name = "rec_frd_stat",nullable = false)
	private Integer recommendStatus;// 推荐状态 常量-参考数据字典
	@Column(name = "rec_frd_url",nullable = false,length=255)
	private String recommendUrl;// 用户推荐的url
	
	public Long getAccountRecommendSequence() {
		return accountRecommendSequence;
	}
	public void setAccountRecommendSequence(Long accountRecommendSequence) {
		this.accountRecommendSequence = accountRecommendSequence;
	}
	public Long getAccountSequence() {
		return accountSequence;
	}
	public void setAccountSequence(Long accountSequence) {
		this.accountSequence = accountSequence;
	}
	public String getFriendId() {
		return friendId;
	}
	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
	public Integer getFriendIdType() {
		return friendIdType;
	}
	public void setFriendIdType(Integer friendIdType) {
		this.friendIdType = friendIdType;
	}
	public Date getRecommendDate() {
		return recommendDate;
	}
	public void setRecommendDate(Date recommendDate) {
		this.recommendDate = recommendDate;
	}
	public Integer getRecommendStatus() {
		return recommendStatus;
	}
	public void setRecommendStatus(Integer recommendStatus) {
		this.recommendStatus = recommendStatus;
	}
	public String getRecommendUrl() {
		return recommendUrl;
	}
	public void setRecommendUrl(String recommendUrl) {
		this.recommendUrl = recommendUrl;
	}

}
