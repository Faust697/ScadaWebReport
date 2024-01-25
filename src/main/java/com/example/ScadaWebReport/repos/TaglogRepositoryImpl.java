package com.example.ScadaWebReport.repos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties.Servlet;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Repository;

import com.example.ScadaWebReport.Entity.Taglog.Taglog;
import com.example.ScadaWebReport.services.dataProcessingService;

@ConditionalOnProperty(name = "spring.datasource.driver-class-name", havingValue = "org.postgresql.Driver") 
@Repository
public class TaglogRepositoryImpl implements TaglogRepo {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private Environment env;

    
    @Override
    @SuppressWarnings("unchecked")
    public List<Taglog> findLatestLogForEachTag(String tableName, String tagIdString, String notNull) {
        // Проверяем текущий диалект базы данных
        String currentDialect = env.getProperty("spring.jpa.properties.hibernate.dialect");

        if (currentDialect != null && currentDialect.contains("org.hibernate.dialect.PostgreSQLDialect")) {
            LocalDateTime fifteenMinutesAgo = LocalDateTime.now().minus(15, ChronoUnit.MINUTES);

            String query = "SELECT DISTINCT ON (tag_id) tag_id, taglog_id, data_value, logtime, logdate, timesource, qualifier "
                    + "FROM logs." + tableName
                    + " WHERE tag_id IN (" + tagIdString + ") "
                    + notNull
                    + " AND logdate >= :fifteenMinutesAgo " // Измененный фильтр
                    + " ORDER BY tag_id, logdate DESC";
      
            
            List<Taglog> result = entityManager.createNativeQuery(query, Taglog.class)
                    .setParameter("fifteenMinutesAgo", fifteenMinutesAgo)
                    .getResultList();

            entityManager.close();

            return result;
        }
        return null;
    }
    
    //Вынести это отсюда
	public String actualTable(int month, int year)
	{
		String monthIns=null;
		LocalDate currentDate=null;
		if(month==0 && year==0)
		{
		currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");	 
		String formatCurrentMonth = currentDate.format(formatter);
		return "logs."+"\"tag_log_" + formatCurrentMonth + "\"";
		}
		//Если вдруг дата начала и дата конца в разных месяцах
		else
			if(month<10)
				monthIns="0"+month;
			else 
				monthIns=String.valueOf(month);
		
		
			return	"logs."+"\"tag_log_"+year+"-"+monthIns+ "\"";
	}

	//Метод для фильтра по датам.
	@Override
	public Taglog findFirstByTagIdAndLogdateBetweenOrderByLogdateDesc(
			String tagId,
			String type,
			LocalDateTime startDate,
			LocalDateTime endDate) {
		
		String table=actualTable(0,0);;
		//Если вдруг дата начала и дата конца в разных месяцах
		if(startDate.getMonthValue() < endDate.getMonthValue() || startDate.getYear() < endDate.getYear() )
		{
			if(type=="ASC")
			table = actualTable(startDate.getMonthValue(),startDate.getYear());
			
			if(type=="DESC")
			table = actualTable(endDate.getMonthValue(),endDate.getYear());
				
			
		}
		else {
		System.out.print(startDate.getMonthValue());
		table = actualTable(startDate.getMonthValue(),startDate.getYear());
		}
		
		 String query = "SELECT * FROM "
				 +table 
                 +" WHERE tag_id =  " + tagId.toString()
                 +" AND logdate BETWEEN "
                 + "\'"+startDate+"\'"
                 + " AND "
                 + "\'"+endDate +"\'"
                 + " AND data_value>0"
                 +" ORDER BY logdate "
                 + type 
                 +" LIMIT 1";
		 System.out.println(query);
  try {
      return (Taglog) entityManager.createNativeQuery(query, Taglog.class)
              .getSingleResult();
  } catch (NoResultException e) {
      return null;
  }
	
	}


   
	@Override
	public List<Taglog> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Taglog> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Taglog> findAllById(Iterable<Long> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Taglog> List<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S extends Taglog> S saveAndFlush(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Taglog> List<S> saveAllAndFlush(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllInBatch(Iterable<Taglog> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Long> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllInBatch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Taglog getOne(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Taglog getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Taglog getReferenceById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Taglog> List<S> findAll(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Taglog> List<S> findAll(Example<S> example, Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Taglog> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Taglog> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Taglog> findById(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public boolean existsById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Taglog entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllById(Iterable<? extends Long> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(Iterable<? extends Taglog> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S extends Taglog> Optional<S> findOne(Example<S> example) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public <S extends Taglog> Page<S> findAll(Example<S> example, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Taglog> long count(Example<S> example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <S extends Taglog> boolean exists(Example<S> example) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <S extends Taglog, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Taglog findFirstByOrderByTaglogId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Taglog findFirstByOrderByTaglogIdAsc() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<Taglog> findLatestLogForEachTag(String tableName) {
		// TODO Auto-generated method stub
		return null;
	}











	



	
}