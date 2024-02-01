package com.example.ScadaWebReport.Entity.Mongo;


import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Тестовая модель для юзера у которого будет лист регионов
@Document(collection = "TgUsers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TgUserModel {
	@org.springframework.data.annotation.Id
    private String id;
	
	private String name;
	private String chatId;
	private ArrayList<String> region;
	@Getter @Setter
	private boolean verified;
	@Getter @Setter
	private boolean notify;
	

}
