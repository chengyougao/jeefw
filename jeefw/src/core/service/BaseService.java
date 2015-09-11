package core.service;

import core.dao.Dao;
import core.support.BaseParameter;
import core.support.QueryResult;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class BaseService<E> implements Service<E> {
	protected Dao<E> dao;

	public void persist(E entity) {
		this.dao.persist(entity);
	}

	public boolean deleteByPK(Serializable... id) {
		return this.dao.deleteByPK(id);
	}

	public void delete(E entity) {
		this.dao.delete(entity);
	}

	public void deleteByProperties(String[] propName, Object[] propValue) {
		this.dao.deleteByProperties(propName, propValue);
	}

	public void deleteByProperties(String propName, Object propValue) {
		this.dao.deleteByProperties(propName, propValue);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public E get(Serializable id) {
		return this.dao.get(id);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public E getByProerties(String[] propName, Object[] propValue) {
		return this.dao.getByProerties(propName, propValue);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public E getByProerties(String propName, Object propValue) {
		return this.dao.getByProerties(propName, propValue);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public E getByProerties(String[] propName, Object[] propValue, Map<String, String> sortedCondition) {
		return this.dao.getByProerties(propName, propValue, sortedCondition);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public E getByProerties(String propName, Object propValue, Map<String, String> sortedCondition) {
		return this.dao.getByProerties(propName, propValue, sortedCondition);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public E load(Serializable id) {
		return this.dao.load(id);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<E> queryByProerties(String[] propName, Object[] propValue) {
		return this.dao.queryByProerties(propName, propValue);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<E> queryByProerties(String propName, Object propValue) {
		return this.dao.queryByProerties(propName, propValue);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<E> queryByProerties(String[] propName, Object[] propValue, Map<String, String> sortedCondition) {
		return this.dao.queryByProerties(propName, propValue, sortedCondition);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<E> queryByProerties(String propName, Object propValue, Map<String, String> sortedCondition) {
		return this.dao.queryByProerties(propName, propValue, sortedCondition);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<E> queryByProerties(String[] propName, Object[] propValue, Map<String, String> sortedCondition, Integer top) {
		return this.dao.queryByProerties(propName, propValue, sortedCondition, top);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<E> queryByProerties(String[] propName, Object[] propValue, Integer top) {
		return this.dao.queryByProerties(propName, propValue, top);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<E> queryByProerties(String propName, Object propValue, Map<String, String> sortedCondition, Integer top) {
		return this.dao.queryByProerties(propName, propValue, sortedCondition, top);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<E> queryByProerties(String propName, Object propValue, Integer top) {
		return this.dao.queryByProerties(propName, propValue, top);
	}

	public E merge(E entity) {
		return this.dao.merge(entity);
	}

	public void update(E entity) {
		this.dao.update(entity);
	}

	public void updateByProperties(String[] conditionName, Object[] conditionValue, String[] propertyName, Object[] propertyValue) {
		this.dao.updateByProperties(conditionName, conditionValue, propertyName, propertyValue);
	}

	public void updateByProperties(String conditionName, Object conditionValue, String[] propertyName, Object[] propertyValue) {
		this.dao.updateByProperties(conditionName, conditionValue, propertyName, propertyValue);
	}

	public void updateByProperties(String[] conditionName, Object[] conditionValue, String propertyName, Object propertyValue) {
		this.dao.updateByProperties(conditionName, conditionValue, propertyName, propertyValue);
	}

	public void updateByProperties(String conditionName, Object conditionValue, String propertyName, Object propertyValue) {
		this.dao.updateByProperties(conditionName, conditionValue, propertyName, propertyValue);
	}

	public void update(E entity, Serializable oldId) {
		this.dao.update(entity, oldId);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<E> doQueryAll() {
		return this.dao.doQueryAll();
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<E> doQueryAll(Map<String, String> sortedCondition, Integer top) {
		return this.dao.doQueryAll(sortedCondition, top);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<E> doQueryAll(Integer top) {
		return this.dao.doQueryAll(top);
	}

	public void evict(E entity) {
		this.dao.evict(entity);
	}

	public void clear() {
		this.dao.clear();
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Long countAll() {
		return this.dao.countAll();
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Long doCount(BaseParameter parameter) {
		return this.dao.doCount(parameter);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<E> doQuery(BaseParameter parameter) {
		return this.dao.doQuery(parameter);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public QueryResult<E> doPaginationQuery(BaseParameter parameter) {
		return this.dao.doPaginationQuery(parameter);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public QueryResult<E> doPaginationQuery(BaseParameter parameter, boolean bool) {
		return this.dao.doPaginationQuery(parameter, bool);
	}
}
