package com.tdd.di.core;

public class Segment {
	private String id;
	private String uavtStreetCode;
	private String name;
	private String startLongitude;
	private String startLatitute;
	private String centerLongitude;
	private String centerLatitute;
	private String endLongitude;
	private String endLatitute;
	private String town;
	private String city;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUavtStreetCode() {
		return uavtStreetCode;
	}

	public void setUavtStreetCode(String uavtStreetCode) {
		this.uavtStreetCode = uavtStreetCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartLongitude() {
		return startLongitude;
	}

	public void setStartLongitude(String startLongitude) {
		this.startLongitude = startLongitude;
	}

	public String getStartLatitute() {
		return startLatitute;
	}

	public void setStartLatitute(String startLatitute) {
		this.startLatitute = startLatitute;
	}

	public String getCenterLongitude() {
		return centerLongitude;
	}

	public void setCenterLongitude(String centerLongitude) {
		this.centerLongitude = centerLongitude;
	}

	public String getCenterLatitute() {
		return centerLatitute;
	}

	public void setCenterLatitute(String centerLatitute) {
		this.centerLatitute = centerLatitute;
	}

	public String getEndLongitude() {
		return endLongitude;
	}

	public void setEndLongitude(String endLongitude) {
		this.endLongitude = endLongitude;
	}

	public String getEndLatitute() {
		return endLatitute;
	}

	public void setEndLatitute(String endLatitute) {
		this.endLatitute = endLatitute;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
