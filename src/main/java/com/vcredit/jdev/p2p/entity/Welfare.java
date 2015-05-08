package com.vcredit.jdev.p2p.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



//后台人员操作记录
@Entity
@Table(name = "t_welfare")
public class Welfare {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_welfare_seq", unique = true, nullable = false)
	private Long welfareSequence;// 流水号
	@Column(name = "wf_name", nullable = false, length = 30)
	private String welfareName;// 福利名称
	@Column(name = "wf_desc", nullable = false, length = 60)
	private String welfareDescription;// 福利详细描述
	@Column(name = "wf_quota", nullable = false, precision = 12, scale = 2)
	private BigDecimal welfareQuota;// 福利额度
	@Column(name = "wf_edate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date welfareExpiredDate;// 失效时间
	@Column(name = "t_res_seq", nullable = false)
	private Long welfareImage;// 福利图片
	@Column(name = "wf_type", nullable = false)
	private Integer welfareType;// 福利类型 常量-参考数据字典
	@Column(name = "wf_cdate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date welfareCreateDate;// 创建时间
	@Column(name = "wf_total")//总数量
	private Integer welfareTotal;
	@Column(name = "wf_cc")//库存数量
	private Integer welfareCurrentCount;
	@Column(name = "wf_acond",length=90)//激活条件
	private String welfareActiveCondition;

	public Long getWelfareSequence() {
		return welfareSequence;
	}
	public void setWelfareSequence(Long welfareSequence) {
		this.welfareSequence = welfareSequence;
	}
	public String getWelfareName() {
		return welfareName;
	}
	public void setWelfareName(String welfareName) {
		this.welfareName = welfareName;
	}
	public String getWelfareDescription() {
		return welfareDescription;
	}
	public void setWelfareDescription(String welfareDescription) {
		this.welfareDescription = welfareDescription;
	}
	public BigDecimal getWelfareQuota() {
		return welfareQuota;
	}
	public void setWelfareQuota(BigDecimal welfareQuota) {
		this.welfareQuota = welfareQuota;
	}
	public Date getWelfareExpiredDate() {
		return welfareExpiredDate;
	}
	public void setWelfareExpiredDate(Date welfareExpiredDate) {
		this.welfareExpiredDate = welfareExpiredDate;
	}
	public Long getWelfareImage() {
		return welfareImage;
	}
	public void setWelfareImage(Long welfareImage) {
		this.welfareImage = welfareImage;
	}
	public Integer getWelfareType() {
		return welfareType;
	}
	public void setWelfareType(Integer welfareType) {
		this.welfareType = welfareType;
	}
	public Date getWelfareCreateDate() {
		return welfareCreateDate;
	}
	public void setWelfareCreateDate(Date welfareCreateDate) {
		this.welfareCreateDate = welfareCreateDate;
	}
	public Integer getWelfareTotal() {
		return welfareTotal;
	}
	public void setWelfareTotal(Integer welfareTotal) {
		this.welfareTotal = welfareTotal;
	}
	public Integer getWelfareCurrentCount() {
		return welfareCurrentCount;
	}
	public void setWelfareCurrentCount(Integer welfareCurrentCount) {
		this.welfareCurrentCount = welfareCurrentCount;
	}
	public String getWelfareActiveCondition() {
		return welfareActiveCondition;
	}
	public void setWelfareActiveCondition(String welfareActiveCondition) {
		this.welfareActiveCondition = welfareActiveCondition;
	}
	
}
