package com.example.ScadaWebReport.Entity.Mongo;



import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "static_info_well") // Указываем имя коллекции в MongoDB
public class StaticInfoWellModel {
	
	@javax.persistence.Id
	private int Id;

	private String motorStatusId;//
	private String LastRunId;//
	private String currentFlowId;//
	private String totalFlowId;//
	private String powerUsageTotalId;//
	private String name;//
	private String scadaStatus;//
	private String explanation;//
	private String cameraIp;//
	private String region;//
	private String coordinates;//
	
	
	
	public StaticInfoWellModel(int id, String motorStatusId, String lastRunId, String currentFlowId,
			String totalFlowId, String powerUsageTotalId, String name, String scadaStatus, String explanation,
			String cameraIp, String region, String coordinates) {
		super();
		Id = id;
		this.motorStatusId = motorStatusId;
		LastRunId = lastRunId;
		this.currentFlowId = currentFlowId;
		this.totalFlowId = totalFlowId;
		this.powerUsageTotalId = powerUsageTotalId;
		this.name = name;
		this.scadaStatus = scadaStatus;
		this.explanation = explanation;
		this.cameraIp = cameraIp;
		this.region = region;
		this.coordinates = coordinates;
	}

	
	
	
	
	
	public StaticInfoWellModel() {
		super();
		// TODO Auto-generated constructor stub
	}






	public int getId() {
		return Id;
	}






	public void setId(int id) {
		Id = id;
	}






	public String getMotorStatusId() {
		return motorStatusId;
	}






	public void setMotorStatusId(String motorStatusId) {
		this.motorStatusId = motorStatusId;
	}






	public String getLastRunId() {
		return LastRunId;
	}






	public void setLastRunId(String lastRunId) {
		LastRunId = lastRunId;
	}






	public String getCurrentFlowId() {
		return currentFlowId;
	}






	public void setCurrentFlowId(String currentFlowId) {
		this.currentFlowId = currentFlowId;
	}






	public String getTotalFlowId() {
		return totalFlowId;
	}






	public void setTotalFlowId(String currentTotalFlowId) {
		this.totalFlowId = currentTotalFlowId;
	}






	public String getPowerUsageTotalId() {
		return powerUsageTotalId;
	}






	public void setPowerUsageTotalId(String powerUsageTotalId) {
		this.powerUsageTotalId = powerUsageTotalId;
	}






	public String getName() {
		return name;
	}






	public void setName(String name) {
		this.name = name;
	}






	public String getScadaStatus() {
		return scadaStatus;
	}






	public void setScadaStatus(String scadaStatus) {
		this.scadaStatus = scadaStatus;
	}






	public String getExplanation() {
		return explanation;
	}






	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}






	public String getCameraIp() {
		return cameraIp;
	}






	public void setCameraIp(String cameraIp) {
		this.cameraIp = cameraIp;
	}






	public String getRegion() {
		return region;
	}






	public void setRegion(String region) {
		this.region = region;
	}






	public String getCoordinates() {
		return coordinates;
	}






	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}
	
	
	

}
