package com.example.ScadaWebReport.Entity.Mongo;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Document(collection = "NotificationObjects")
@Data
@AllArgsConstructor
public class NotificationObjectModel {
	private String id;
	private String wellId;
	private String name;
	private String region;
	private String totalFlowValue;
	
	private boolean notificationStatus;
	

}
