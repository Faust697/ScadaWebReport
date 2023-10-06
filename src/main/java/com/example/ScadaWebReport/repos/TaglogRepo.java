package com.example.ScadaWebReport.repos;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.example.ScadaWebReport.Entity.Taglog.Taglog;


@ConditionalOnProperty(name = "spring.datasource.driver-class-name", havingValue = "org.postgresql.Driver") 
public interface TaglogRepo extends JpaRepository<Taglog, Long> {

Taglog findFirstByOrderByTaglogId() ;

Taglog findFirstByOrderByTaglogIdAsc();



List<Taglog> findLatestLogForEachTag(@Param("tableName") String tableName);

List<Taglog> findLatestLogForEachTag(String tableName, String tagIdString, String notNull);
Taglog findFirstByTagIdAndLogdateBetweenOrderByLogdateDesc(String tagId, String type, LocalDateTime startDate, LocalDateTime endDate);







}
