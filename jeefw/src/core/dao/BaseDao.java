package core.dao;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import core.support.BaseParameter;
import core.support.QueryResult;

public class BaseDao<E> implements Dao<E> {
	protected final Logger log = Logger.getLogger(BaseDao.class);
	private static Map<String, Method> MAP_METHOD = new HashMap();
	private SessionFactory sessionFactory;
	protected Class<E> entityClass;
	
	private JdbcTemplate jdbcTemplate;
	

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Resource(name = "sessionFactory")
	public void setSF(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Resource(name = "jdbcTemplate")
	public void setJT(JdbcTemplate jdbcTemplate) {
		setJdbcTemplate(jdbcTemplate);
	}

	public BaseDao(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	public void persist(E entity) {
		getSession().save(entity);
	}

	public boolean deleteByPK(Serializable... id) {
		boolean result = false;
		if ((id != null) && (id.length > 0)) {
			for (int i = 0; i < id.length; i++) {
				E entity = get(id[i]);
				if (entity != null) {
					getSession().delete(entity);
					result = true;
				}
			}
		}
		return result;
	}

	public void deleteByProperties(String[] propName, Object[] propValue) {
		if ((propName != null) && (propName.length > 0) && (propValue != null) && (propValue.length > 0) && (propValue.length == propName.length)) {
			StringBuffer sb = new StringBuffer("delete from " + this.entityClass.getName() + " o where 1=1 ");
			appendQL(sb, propName, propValue);
			Query query = getSession().createQuery(sb.toString());
			setParameter(query, propName, propValue);
			query.executeUpdate();
		}
	}

	public void delete(E entity) {
		getSession().delete(entity);
	}

	public void deleteByProperties(String propName, Object propValue) {
		deleteByProperties(new String[] { propName }, new Object[] { propValue });
	}

	public void updateByProperties(String[] conditionName, Object[] conditionValue, String[] propertyName, Object[] propertyValue) {
		if ((propertyName != null) && (propertyName.length > 0) && (propertyValue != null) && (propertyValue.length > 0) && (propertyName.length == propertyValue.length) && (conditionValue != null)
				&& (conditionValue.length > 0)) {
			StringBuffer sb = new StringBuffer();
			sb.append("update " + this.entityClass.getName() + " o set ");
			for (int i = 0; i < propertyName.length; i++) {
				sb.append(propertyName[i] + " = :p_" + propertyName[i] + ",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(" where 1=1 ");
			appendQL(sb, conditionName, conditionValue);
			Query query = getSession().createQuery(sb.toString());
			for (int i = 0; i < propertyName.length; i++) {
				query.setParameter("p_" + propertyName[i], propertyValue[i]);
			}
			setParameter(query, conditionName, conditionValue);
			query.executeUpdate();
		} else {
			throw new IllegalArgumentException("Method updateByProperties in BaseDao argument is illegal!");
		}
	}

	public void updateByProperties(String[] conditionName, Object[] conditionValue, String propertyName, Object propertyValue) {
		updateByProperties(conditionName, conditionValue, new String[] { propertyName }, new Object[] { propertyValue });
	}

	public void updateByProperties(String conditionName, Object conditionValue, String[] propertyName, Object[] propertyValue) {
		updateByProperties(new String[] { conditionName }, new Object[] { conditionValue }, propertyName, propertyValue);
	}

	public void updateByProperties(String conditionName, Object conditionValue, String propertyName, Object propertyValue) {
		updateByProperties(new String[] { conditionName }, new Object[] { conditionValue }, new String[] { propertyName }, new Object[] { propertyValue });
	}

	public void update(E entity) {
		getSession().update(entity);
	}

	public void update(E entity, Serializable oldId) {
		deleteByPK(new Serializable[] { oldId });
		persist(entity);
	}

	public E merge(E entity) {
		return (E) getSession().merge(entity);
	}

	public E getByProerties(String[] propName, Object[] propValue, Map<String, String> sortedCondition) {
		if ((propName != null) && (propName.length > 0) && (propValue != null) && (propValue.length > 0) && (propValue.length == propName.length)) {
			StringBuffer sb = new StringBuffer("select o from " + this.entityClass.getName() + " o where 1=1 ");
			appendQL(sb, propName, propValue);
			if ((sortedCondition != null) && (sortedCondition.size() > 0)) {
				sb.append(" order by ");
				for (Map.Entry<String, String> e : sortedCondition.entrySet()) {
					sb.append((String) e.getKey() + " " + (String) e.getValue() + ",");
				}
				sb.deleteCharAt(sb.length() - 1);
			}
			Query query = getSession().createQuery(sb.toString());
			setParameter(query, propName, propValue);
			List<E> list = query.list();
			if ((list != null) && (((List) list).size() > 0)) {
				return list.get(0);
			}
		}
		return null;
	}

	public E get(Serializable id) {
		return (E) getSession().get(entityClass, id);
	}

	public E load(Serializable id) {
		return (E)getSession().load(this.entityClass, id);
	}

	public E getByProerties(String[] propName, Object[] propValue) {
		return getByProerties(propName, propValue, null);
	}

	public E getByProerties(String propName, Object propValue) {
		return getByProerties(new String[] { propName }, new Object[] { propValue });
	}

	public E getByProerties(String propName, Object propValue, Map<String, String> sortedCondition) {
		return getByProerties(new String[] { propName }, new Object[] { propValue }, sortedCondition);
	}

	public List<E> queryByProerties(String[] propName, Object[] propValue, Map<String, String> sortedCondition, Integer top) {
		if ((propName != null) && (propValue != null) && (propValue.length == propName.length)) {
			StringBuffer sb = new StringBuffer("select o from " + this.entityClass.getName() + " o where 1=1 ");
			appendQL(sb, propName, propValue);
			if ((sortedCondition != null) && (sortedCondition.size() > 0)) {
				sb.append(" order by ");
				for (Map.Entry<String, String> e : sortedCondition.entrySet()) {
					sb.append((String) e.getKey() + " " + (String) e.getValue() + ",");
				}
				sb.deleteCharAt(sb.length() - 1);
			}
			Query query = getSession().createQuery(sb.toString());
			setParameter(query, propName, propValue);
			if (top != null) {
				query.setFirstResult(0);
				query.setMaxResults(top.intValue());
			}
			return query.list();
		}
		return null;
	}

	public List<E> queryByProerties(String[] propName, Object[] propValue, Integer top) {
		return queryByProerties(propName, propValue, null, top);
	}

	public List<E> queryByProerties(String[] propName, Object[] propValue, Map<String, String> sortedCondition) {
		return queryByProerties(propName, propValue, sortedCondition, null);
	}

	public List<E> queryByProerties(String propName, Object propValue, Map<String, String> sortedCondition, Integer top) {
		return queryByProerties(new String[] { propName }, new Object[] { propValue }, sortedCondition, top);
	}

	public List<E> queryByProerties(String propName, Object propValue, Map<String, String> sortedCondition) {
		return queryByProerties(new String[] { propName }, new Object[] { propValue }, sortedCondition, null);
	}

	public List<E> queryByProerties(String propName, Object propValue, Integer top) {
		return queryByProerties(new String[] { propName }, new Object[] { propValue }, null, top);
	}

	public List<E> queryByProerties(String[] propName, Object[] propValue) {
		return queryByProerties(propName, propValue, null, null);
	}

	public List<E> queryByProerties(String propName, Object propValue) {
		return queryByProerties(new String[] { propName }, new Object[] { propValue }, null, null);
	}

	public Long countAll() {
		return (Long) getSession().createQuery("select count(*) from " + this.entityClass.getName()).uniqueResult();
	}

	public void clear() {
		getSession().clear();
	}

	public void evict(E entity) {
		getSession().evict(entity);
	}

	public List<E> doQueryAll(Map<String, String> sortedCondition, Integer top) {
		Criteria criteria = getSession().createCriteria(this.entityClass);
		if ((sortedCondition != null) && (sortedCondition.size() > 0)) {
			for (Iterator<String> it = sortedCondition.keySet().iterator(); it.hasNext();) {
				String pm = (String) it.next();
				if ("DESC".equals(sortedCondition.get(pm))) {
					criteria.addOrder(Order.desc(pm));
				} else if ("ASC".equals(sortedCondition.get(pm))) {
					criteria.addOrder(Order.asc(pm));
				}
			}
		}
		if (top != null) {
			criteria.setMaxResults(top.intValue());
			criteria.setFirstResult(0);
		}
		return criteria.list();
	}

	public List<E> doQueryAll() {
		return doQueryAll(null, null);
	}

	public List<E> doQueryAll(Integer top) {
		return doQueryAll(null, top);
	}

	public Long doCount(BaseParameter param) {
		if (param == null) {
			return null;
		}
		Criteria criteria = getSession().createCriteria(this.entityClass);
		processQuery(criteria, param);
		try {
			criteria.setProjection(Projections.rowCount());
			return Long.valueOf(((Number) criteria.uniqueResult()).longValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<E> doQuery(BaseParameter param) {
		if (param == null) {
			return null;
		}
		Criteria criteria = getSession().createCriteria(this.entityClass);
		processQuery(criteria, param);
		try {
			if ((param.getSortedConditions() != null) && (param.getSortedConditions().size() > 0)) {
				Map<String, String> map = param.getSortedConditions();
				for (Iterator<String> it = param.getSortedConditions().keySet().iterator(); it.hasNext();) {
					String pm = (String) it.next();
					if ("DESC".equals(map.get(pm))) {
						criteria.addOrder(Order.desc(pm));
					} else if ("ASC".equals(map.get(pm))) {
						criteria.addOrder(Order.asc(pm));
					}
				}
			}
			return criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public QueryResult<E> doPaginationQuery(BaseParameter param) {
		return doPaginationQuery(param, true);
	}

	public QueryResult<E> doPaginationQuery(BaseParameter param, boolean bool) {
		if (param == null) {
			return null;
		}
		Criteria criteria = getSession().createCriteria(this.entityClass);
		if (bool) {
			processQuery(criteria, param);
		} else {
			extendprocessQuery(criteria, param);
		}
		try {
			QueryResult<E> qr = new QueryResult();
			criteria.setProjection(Projections.rowCount());
			qr.setTotalCount(Long.valueOf(((Number) criteria.uniqueResult()).longValue()));
			if (qr.getTotalCount().longValue() > 0L) {
				if ((param.getSortedConditions() != null) && (param.getSortedConditions().size() > 0)) {
					Map<String, String> map = param.getSortedConditions();
					for (Iterator<String> it = param.getSortedConditions().keySet().iterator(); it.hasNext();) {
						String pm = (String) it.next();
						if ("DESC".equalsIgnoreCase((String) map.get(pm))) {
							criteria.addOrder(Order.desc(pm));
						} else if ("ASC".equalsIgnoreCase((String) map.get(pm))) {
							criteria.addOrder(Order.asc(pm));
						}
					}
				}
				criteria.setProjection(null);
				criteria.setMaxResults(param.getMaxResults().intValue());
				criteria.setFirstResult(param.getFirstResult().intValue());
				qr.setResultList(criteria.list());
			} else {
				qr.setResultList(new ArrayList());
			}
			return qr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void appendQL(StringBuffer sb, String[] propName, Object[] propValue) {
		for (int i = 0; i < propName.length; i++) {
			String name = propName[i];
			Object value = propValue[i];
			if (((value instanceof Object[])) || ((value instanceof Collection))) {
				Object[] arraySerializable = (Object[]) value;
				if ((arraySerializable != null) && (arraySerializable.length > 0)) {
					sb.append(" and o." + name + " in (:" + name.replace(".", "") + ")");
				}
			} else if (value == null) {
				sb.append(" and o." + name + " is null ");
			} else {
				sb.append(" and o." + name + "=:" + name.replace(".", ""));
			}
		}
	}

	private void setParameter(Query query, String[] propName, Object[] propValue) {
		for (int i = 0; i < propName.length; i++) {
			String name = propName[i];
			Object value = propValue[i];
			if (value != null) {
				if ((value instanceof Object[])) {
					query.setParameterList(name.replace(".", ""), (Object[]) value);
				} else if ((value instanceof Collection)) {
					query.setParameterList(name.replace(".", ""), (Collection) value);
				} else {
					query.setParameter(name.replace(".", ""), value);
				}
			}
		}
	}

	protected void buildSorted(BaseParameter param, StringBuffer hql) {
		if ((param.getSortedConditions() != null) && (param.getSortedConditions().size() > 0)) {
			hql.append(" order by ");
			Map<String, String> sorted = param.getSortedConditions();
			for (Iterator<String> it = sorted.keySet().iterator(); it.hasNext();) {
				String col = (String) it.next();
				hql.append(col + " " + (String) sorted.get(col) + ",");
			}
			hql.deleteCharAt(hql.length() - 1);
		}
	}

	private String transferColumn(String queryCondition) {
		return queryCondition.substring(queryCondition.indexOf('_', 1) + 1);
	}

	protected void setParameter(Map<String, Object> mapParameter, Query query) {
		for (Iterator<String> it = mapParameter.keySet().iterator(); it.hasNext();) {
			String parameterName = (String) it.next();
			Object value = mapParameter.get(parameterName);
			query.setParameter(parameterName, value);
		}
	}

	protected Map handlerConditions(BaseParameter param) throws Exception {
		Map staticConditions = core.util.BeanUtils.describe(param);
		Map<String, Object> dynamicConditions = param.getQueryDynamicConditions();
		if (dynamicConditions.size() > 0) {
			for (Iterator it = staticConditions.keySet().iterator(); it.hasNext();) {
				String key = (String) it.next();
				Object value = staticConditions.get(key);
				if ((key.startsWith("$")) && (value != null) && (!"".equals(value))) {
					dynamicConditions.put(key, value);
				}
			}
			staticConditions = dynamicConditions;
		}
		return staticConditions;
	}

	private Method getMethod(String name) {
		if (!MAP_METHOD.containsKey(name)) {
			Class<Restrictions> clazz = Restrictions.class;
			Class[] paramType = { String.class, Object.class };
			Class[] likeParamType = { String.class, String.class, MatchMode.class };
			Class[] isNullType = { String.class };
			try {
				Method method = null;
				if ("like".equals(name)) {
					method = clazz.getMethod(name, likeParamType);
				} else if ("isNull".equals(name)) {
					method = clazz.getMethod(name, isNullType);
				} else {
					method = clazz.getMethod(name, paramType);
				}
				MAP_METHOD.put(name, method);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return (Method) MAP_METHOD.get(name);
	}

	private Method getExtendMethod(String name) {
		if (!MAP_METHOD.containsKey(name)) {
			Class<Restrictions> clazz = Restrictions.class;
			Class[] paramType = { String.class, Object.class };
			Class[] likeParamType = { String.class, String.class, MatchMode.class };
			Class[] isNullType = { String.class };
			try {
				Method method = null;
				if ("like".equals(name)) {
					method = clazz.getMethod(name, likeParamType);
				} else if ("isNull".equals(name)) {
					method = clazz.getMethod(name, isNullType);
				} else if (!"IN".equals(name.toUpperCase())) {
					method = clazz.getMethod(name, paramType);
				}
				MAP_METHOD.put(name, method);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return (Method) MAP_METHOD.get(name);
	}

	private String getOpt(String value) {
		return value.substring(0, value.indexOf('_', 1)).substring(1);
	}

	private String getPropName(String value) {
		return value.substring(value.indexOf('_', 1) + 1);
	}

	private void processQuery(Criteria criteria, BaseParameter param) {
		try {
			Map<String, Object> staticConditionMap = core.util.BeanUtils.describeAvailableParameter(param);
			Map<String, Object> dynamicConditionMap = param.getQueryDynamicConditions();
			Disjunction disjunction = Restrictions.disjunction();
			String prop;
			if ((staticConditionMap != null) && (staticConditionMap.size() > 0)) {
				for (Map.Entry<String, Object> e : staticConditionMap.entrySet()) {
					Object value = e.getValue();
					if ((value != null) && ((!(value instanceof String)) || (!"".equals((String) value)))) {
						prop = getPropName((String) e.getKey());
						String methodName = getOpt((String) e.getKey());
						Method m = getMethod(methodName);
						if ("like".equals(methodName)) {
							if (param.getFlag().equals("OR")) {
								criteria.add(disjunction.add((Criterion) m.invoke(Restrictions.class, new Object[] { prop, value, MatchMode.ANYWHERE })));
							} else {
								criteria.add((Criterion) m.invoke(Restrictions.class, new Object[] { prop, value, MatchMode.ANYWHERE }));
							}
						} else if (("isNull".equals(methodName)) && ((value instanceof Boolean))) {
							if (((Boolean) value).booleanValue()) {
								if (param.getFlag().equals("OR")) {
									criteria.add(disjunction.add(Restrictions.isNull(prop)));
								} else {
									criteria.add(Restrictions.isNull(prop));
								}
							} else if (param.getFlag().equals("OR")) {
								criteria.add(disjunction.add(Restrictions.isNotNull(prop)));
							} else {
								criteria.add(Restrictions.isNotNull(prop));
							}
						} else if (param.getFlag().equals("OR")) {
							criteria.add(disjunction.add((Criterion) m.invoke(Restrictions.class, new Object[] { prop, value })));
						} else {
							criteria.add((Criterion) m.invoke(Restrictions.class, new Object[] { prop, value }));
						}
					}
				}
			}
			if ((dynamicConditionMap != null) && (dynamicConditionMap.size() > 0)) {
				Object bean = this.entityClass.newInstance();
				Object map = new HashMap();
				for (Map.Entry<String, Object> e : dynamicConditionMap.entrySet()) {
					((Map) map).put(getPropName((String) e.getKey()), e.getValue());
				}
				org.apache.commons.beanutils.BeanUtils.populate(bean, (Map) map);
				for (Map.Entry<String, Object> e : dynamicConditionMap.entrySet()) {
					String pn = (String) e.getKey();
					String prop1 = getPropName(pn);
					String methodName = getOpt(pn);
					Method m = getMethod(methodName);
					Object value = PropertyUtils.getNestedProperty(bean, prop1);
					if ((value != null) && ((!(value instanceof String)) || (!"".equals((String) value)))) {
						if ("like".equals(methodName)) {
							if (param.getFlag().equals("OR")) {
								criteria.add(disjunction.add((Criterion) m.invoke(Restrictions.class, new Object[] { prop1, value, MatchMode.ANYWHERE })));
							} else {
								criteria.add((Criterion) m.invoke(Restrictions.class, new Object[] { prop1, value, MatchMode.ANYWHERE }));
							}
						} else if (("isNull".equals(methodName)) && ((value instanceof Boolean))) {
							if (((Boolean) value).booleanValue()) {
								if (param.getFlag().equals("OR")) {
									criteria.add(disjunction.add(Restrictions.isNull(prop1)));
								} else {
									criteria.add(Restrictions.isNull(prop1));
								}
							} else if (param.getFlag().equals("OR")) {
								criteria.add(disjunction.add(Restrictions.isNotNull(prop1)));
							} else {
								criteria.add(Restrictions.isNotNull(prop1));
							}
						} else if (param.getFlag().equals("OR")) {
							criteria.add(disjunction.add((Criterion) m.invoke(Restrictions.class, new Object[] { prop1, value })));
						} else {
							criteria.add((Criterion) m.invoke(Restrictions.class, new Object[] { prop1, value }));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void extendprocessQuery(Criteria criteria, BaseParameter param) {
		try {
			Map<String, Object> staticConditionMap = core.util.BeanUtils.describeAvailableParameter(param);
			Map<String, Object> dynamicConditionMap = param.getQueryDynamicConditions();
			String prop;
			if ((staticConditionMap != null) && (staticConditionMap.size() > 0)) {
				for (Map.Entry<String, Object> e : staticConditionMap.entrySet()) {
					Object value = e.getValue();
					if ((value != null) && ((!(value instanceof String)) || (!"".equals((String) value)))) {
						prop = getPropName((String) e.getKey());
						String methodName = getOpt((String) e.getKey());
						Method m = getExtendMethod(methodName);
						if ("like".equals(methodName)) {
							criteria.add((Criterion) m.invoke(Restrictions.class, new Object[] { prop, value, MatchMode.ANYWHERE }));
						} else if (("isNull".equals(methodName)) && ((value instanceof Boolean))) {
							if (((Boolean) value).booleanValue()) {
								criteria.add(Restrictions.isNull(prop));
							} else {
								criteria.add(Restrictions.isNotNull(prop));
							}
						} else if ((value != null) && ((value instanceof Object[])) && ("IN".equals(methodName.toUpperCase()))) {
							Object[] obj = (Object[]) value;
							criteria.add(Restrictions.in(prop, obj));
						} else {
							criteria.add((Criterion) m.invoke(Restrictions.class, new Object[] { prop, value }));
						}
					}
				}
			}
			if ((dynamicConditionMap != null) && (dynamicConditionMap.size() > 0)) {
				Object bean = this.entityClass.newInstance();
				Object map = new HashMap();
				for (Map.Entry<String, Object> e : dynamicConditionMap.entrySet()) {
					((Map) map).put(getPropName((String) e.getKey()), e.getValue());
				}
				org.apache.commons.beanutils.BeanUtils.populate(bean, (Map) map);
				for (Map.Entry<String, Object> e : dynamicConditionMap.entrySet()) {
					String pn = (String) e.getKey();
					String prop1 = getPropName(pn);
					String methodName = getOpt(pn);
					Method m = getMethod(methodName);
					Object value = PropertyUtils.getNestedProperty(bean, prop1);
					if ((value != null) && ((!(value instanceof String)) || (!"".equals((String) value)))) {
						if ("like".equals(methodName)) {
							criteria.add((Criterion) m.invoke(Restrictions.class, new Object[] { prop1, value, MatchMode.ANYWHERE }));
						} else if (("isNull".equals(methodName)) && ((value instanceof Boolean))) {
							if (((Boolean) value).booleanValue()) {
								criteria.add(Restrictions.isNull(prop1));
							} else {
								criteria.add(Restrictions.isNotNull(prop1));
							}
						} else {
							criteria.add((Criterion) m.invoke(Restrictions.class, new Object[] { prop1, value }));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
