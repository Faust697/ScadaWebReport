package com.example.ScadaWebReport.Entity.Mongo;

import com.example.ScadaWebReport.Entity.Taglog.Taglog;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Well{

	private int Id;
	private String name;
	private String coordinates;
	private String region;
	private String scadaStatus;
	private String motorStatus;
	private String lastRun;
	private String currentFlow;
	private String totalFlow;
	private String powerUsageTotal;
	private String camera;
	private String info;

	
	
/*
	public Well(int id, String name, String coordinates, String region, String scadaStatus, String motorStatus,
			String lastRun, String currentFlow, String totalFlow, String powerUsageTotal, String camera, String info) {
		super();
		Id = id;
		this.name = name;
		this.coordinates = coordinates;
		this.region = region;
		this.scadaStatus = scadaStatus;
		this.motorStatus = motorStatus;
		this.lastRun = lastRun;
		this.currentFlow = currentFlow;
		this.totalFlow = totalFlow;
		this.powerUsageTotal = powerUsageTotal;
		this.camera = camera;
		this.info = info;
	}
*/

	/*
	public int getId() {
		return Id;
	}


	public void setId(int id) {
		Id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCoordinates() {
		return coordinates;
	}


	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}


	public String getRegion() {
		return region;
	}


	public void setRegion(String region) {
		this.region = region;
	}


	public String getScadaStatus() {
		return scadaStatus;
	}


	public void setScadaStatus(String scadaStatus) {
		this.scadaStatus = scadaStatus;
	}


	public String getMotorStatus() {
		return motorStatus;
	}


	public void setMotorStatus(String motorStatus) {
		this.motorStatus = motorStatus;
	}


	public String getLastRun() {
		return lastRun;
	}


	public void setLastRun(String lastRun) {
		this.lastRun = lastRun;
	}


	public String getCurrentFlow() {
		return currentFlow;
	}


	public void setCurrentFlow(String currentFlow) {
		this.currentFlow = currentFlow;
	}


	public String getTotalFlow() {
		return totalFlow;
	}


	public void setTotalFlow(String totalFlow) {
		this.totalFlow = totalFlow;
	}


	public String getPowerUsageTotal() {
		return powerUsageTotal;
	}


	public void setPowerUsageTotal(String powerUsageTotal) {
		this.powerUsageTotal = powerUsageTotal;
	}


	public String getCamera() {
		return camera;
	}


	public void setCamera(String camera) {
		this.camera = camera;
	}


	public String getInfo() {
		return info;
	}


	public void setInfo(String info) {
		this.info = info;
	}*/
}
