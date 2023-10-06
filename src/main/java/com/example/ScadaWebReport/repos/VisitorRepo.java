package com.example.ScadaWebReport.repos;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.ScadaWebReport.Document.MongoDocument.VisitorModel;




@Repository
public interface VisitorRepo extends MongoRepository<VisitorModel, String> {


		List<VisitorModel> findByVisitorIp();

		List<VisitorModel> findAll();
		
		 Optional<VisitorModel> findByVisitorIp(String ipAddress);
		
	
		//Добавление нового посетителя
		default VisitorModel saveOrUpdateVisitor(String visitorIp) {
		    Optional<VisitorModel> existingVisitorOptional = findByVisitorIp(visitorIp);
		    VisitorModel visitor;

		    Instant now = Instant.now();

		    if (existingVisitorOptional.isPresent()) {
		        // Если посетитель с таким айпи уже существует, обновляем его
		        visitor = existingVisitorOptional.get();
		        visitor.setLastVisitTime(now);
		    } else {
		        // Если посетитель с таким айпи не существует, создаем нового
		        visitor = new VisitorModel();
		        visitor.setVisitorIp(visitorIp);
		        visitor.setFirstVisitTime(now);
		        visitor.setLastVisitTime(now);
		    }

		    // Сохраняем или обновляем запись в базе данных
		    return save(visitor);
		}
		

		


		// Метод для получения общего числа записей в коллекции
	    @Query(value = "{ }", count = true)
	    long countAll();
	    
	    
	 // Метод для получения записей, где lastVisitTime не больше 7 дней от текущего времени
	    @Query(value = "{ 'lastVisitTime': { $gte: ?0, $lte: ?1 } }", count = true)
	    int countRecentVisitors(Instant sevenDaysAgo, Instant currentTime);
	
}
