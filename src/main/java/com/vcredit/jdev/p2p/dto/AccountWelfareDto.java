package com.vcredit.jdev.p2p.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

import com.vcredit.jdev.p2p.entity.AccountWelfare;

//用户得到福利，暂时为的红包
/**
 * 用户Dto
 * 
 * @author 周佩
 *
 */
public class AccountWelfareDto extends AccountWelfare {

	private Long welfareSequence;// 流水号
	private String welfareName;// 福利名称
	private String welfareDescription;// 福利详细描述
	private BigDecimal welfareQuota;// 福利额度
	private Date welfareExpiredDate;// 失效时间
	private Long welfareImage;// 福利图片
	private Integer welfareType;// 福利类型 常量-参考数据字典
	private Date welfareCreateDate;// 创建时间
	private Integer welfareTotal;
	private Integer welfareCurrentCount;
	private String welfareActiveCondition;// 激活条件

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
