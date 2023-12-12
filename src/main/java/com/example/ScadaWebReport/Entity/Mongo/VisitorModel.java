package com.example.ScadaWebReport.Entity.Mongo;

import java.time.Instant;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "visitors_counter") // Указываем имя коллекции в MongoDB
public class VisitorModel {

	@Id
	private ObjectId id;
	private String visitorIp;
	private Instant firstVisitTime;
	private Instant lastVisitTime;

	public VisitorModel(ObjectId id, String visitorIp, Instant firstVisitTime, Instant lastVisitTime) {
		super();
		this.id = id;
		this.visitorIp = visitorIp;
		this.firstVisitTime = firstVisitTime;
		this.lastVisitTime = lastVisitTime;
	}

	public VisitorModel() {
		// TODO Auto-generated constructor stub
	}

	public ObjectId getVisitorId() {
		return id;
	}

	public void setVisitorId(ObjectId id) {
		this.id = id;
	}

	public String getVisitorIp() {
		return visitorIp;
	}

	public void setVisitorIp(String visitorIp) {
		this.visitorIp = visitorIp;
	}

	public Instant getFirstVisitTime() {
		return firstVisitTime;
	}

	public void setFirstVisitTime(Instant firstVisitTime) {
		this.firstVisitTime = firstVisitTime;
	}

	public Instant getLastVisitTime() {
		return lastVisitTime;
	}

	public void setLastVisitTime(Instant lastVisitTime) {
		this.lastVisitTime = lastVisitTime;
	}
}