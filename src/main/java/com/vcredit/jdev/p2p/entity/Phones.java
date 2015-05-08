package com.vcredit.jdev.p2p.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//真假手机号
@Entity
@Table(name="t_phoes")
public class Phones {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_phoes_seq", unique = true, nullable = false)
	private Long phonesSequence;// 流水号 
	@Column(name = "real_phos",nullable = false,length=20)
	private String realPhones;
	@Column(name = "fake_phos",nullable = false,length=20)	
	private String fakePhones;
	public Long getPhonesSequence() {
		return phonesSequence;
	}
	public void setPhonesSequence(Long phonesSequence) {
		this.phonesSequence = phonesSequence;
	}
	public String getRealPhones() {
		return realPhones;
	}
	public void setRealPhones(String realPhones) {
		this.realPhones = realPhones;
	}
	public String getFakePhones() {
		return fakePhones;
	}
	public void setFakePhones(String fakePhones) {
		this.fakePhones = fakePhones;
	}
	
}
