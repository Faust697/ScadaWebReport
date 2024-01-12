package com.example.ScadaWebReport.Entity.Mongo;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Document(collection = "NotificationObjects")
@Data
@AllArgsConstructor
public class NotificationObjectModel {
	private String id;
	private String totalFlowId;
	private String totalFlowValue;
	private String totalFlowName;
	private boolean notificationStatus;
	

}
