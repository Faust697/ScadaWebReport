package com.example.ScadaWebReport.Entity.Mongo;


import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Document(collection = "TelegramUsers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelegramUserModel {
	@org.springframework.data.annotation.Id
    private String id;
	
	private String name;
	private String chatId;
	private boolean verified;
	private boolean notify;
	

}
