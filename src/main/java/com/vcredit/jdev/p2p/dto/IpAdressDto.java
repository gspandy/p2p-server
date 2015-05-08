package com.vcredit.jdev.p2p.dto;

public class IpAdressDto {
	private String ip;
	private String country;
	private String area;
	private String province;
	private String city;
	private String district;
	private String carrier;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	
	public String toString(){
		return "*ip:"+ip+" *country:"+country+" *area:"+area+" *province:"+province+
				" *city:"+city+" *district:"+district+" *carrier:"+carrier;
	}
	
	
}
