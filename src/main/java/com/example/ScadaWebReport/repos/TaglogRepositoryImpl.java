package com.example.ScadaWebReport.repos;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Repository;

import com.example.ScadaWebReport.domain.Taglog;

@Repository
public class TaglogRepositoryImpl implements TaglogRepo {

    @PersistenceContext
    private EntityManager entityManager;

    
    @Override
    @SuppressWarnings("unchecked")
    public List<Taglog> findLatestLogForEachTag(String tableName, String tagIdString, String notNull) {
        String query = "SELECT DISTINCT ON (tag_id) tag_id, taglog_id, data_value, logtime, logdate, timesource, qualifier "
                + "FROM logs." + tableName
                + " where tag_id in (" + tagIdString + ") "
                + notNull
                + " ORDER BY tag_id, logdate DESC";

        return entityManager.createNativeQuery(query, Taglog.class).getResultList();
    }
    
    

    
    @Override
    @SuppressWarnings("unchecked")
    public List<Taglog> findLatestLogForEachTag(String tableName) {
    	String query = "SELECT DISTINCT ON (tag_id) tag_id, taglog_id, data_value, logtime, logdate, timesource, qualifier "
                + "FROM logs." + tableName
                + " where tag_id in ('10935',"
                + "'10993',"
                + "'12343',"
                + "'13175',"
                + "'13143',"
                + "'13207',"
                + "'13763',"
                + "'14207',"
                + "'14367',"
                + "'14438',"
                + "'14406',"
                + "'18469',"
                + "'18501',"
                + "'18629',"
                + "'19232',"
                + "'19264',"
                + "'19397',"
                + "'19527',"
                + "'20851',"
                + "'11682',"
                + "'12951',"
                + "'14159',"
                + "'14175',"
                + "'19464',"
                + "'19496',"
                + "'19682',"
                + "'19807',"
                + "'20715',"
                + "'12324',"
                + "'10883',"
                + "'10945',"
                + "'11109',"
                + "'11130',"
                + "'13111',"
                + "'13015',"
                + "'13047',"
                + "'13736',"
                + "'13857',"
                + "'18597',"
                + "'19365',"
                + "'19558',"
                + "'19651',"
                + "'19714',"
                + "'19934',"
                + "'13335',"
                + "'13524',"
                + "'13303',"
                + "'20181',"
                + "'12983'"
                + ")"
                + " ORDER BY tag_id, logdate DESC";


        return entityManager.createNativeQuery(query, Taglog.class).getResultList();
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

	
}