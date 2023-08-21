package com.example.ScadaWebReport.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.example.ScadaWebReport.domain.Taglog;

public interface TaglogRepo extends JpaRepository<Taglog, Long> {

Taglog findFirstByOrderByTaglogId() ;

Taglog findFirstByOrderByTaglogIdAsc();



List<Taglog> findLatestLogForEachTag(@Param("tableName") String tableName);

List<Taglog> findLatestLogForEachTag(String tableName, String tagIdString, String notNull);




}
