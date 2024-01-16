package com.example.ScadaWebReport.Entity.Mongo;


import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Document(collection = "TelegramUsers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelegramUserModel {
	@org.springframework.data.annotation.Id
    private String id;
	
	private String name;
	private String chatId;
	@Getter @Setter
	private boolean verified;
	@Getter @Setter
	private boolean notify;
	
	
	public boolean isVerified() {
        return verified;
    }
	
	public boolean isNotify() {
        return notify;
    }

}
