package com.example.ScadaWebReport.Model.staticInfo;



import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "static_info") // Указываем имя коллекции в MongoDB
public class StaticInfoModel {
	

	private int Id;
	
	private String onlineId;
	private String totalId;
	private String levelId;
	private String name;
	private String persent; 
	private String calibrationStatus;
	private String explanation;
	private String cameraIp;
	
	
	
	public StaticInfoModel(int id, String onlineId, String totalId, String levelId, String name, String persent,
			String calibrationStatus, String explanation, String cameraIp) {
		super();
		Id = id;
		this.onlineId = onlineId;
		this.totalId = totalId;
		this.levelId = levelId;
		this.name = name;
		this.persent = persent;
		this.calibrationStatus = calibrationStatus;
		this.explanation = explanation;
		this.cameraIp = cameraIp;
	}
	
	
	public StaticInfoModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getOnlineId() {
		return onlineId;
	}
	public void setOnlineId(String onlineId) {
		this.onlineId = onlineId;
	}
	public String getTotalId() {
		return totalId;
	}
	public void setTotalId(String totalId) {
		this.totalId = totalId;
	}
	public String getLevelId() {
		return levelId;
	}
	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPersent() {
		return persent;
	}
	public void setPersent(String persent) {
		this.persent = persent;
	}
	public String getCalibrationStatus() {
		return calibrationStatus;
	}
	public void setCalibrationStatus(String calibrationStatus) {
		this.calibrationStatus = calibrationStatus;
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
	

}
