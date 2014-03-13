package edu.zju.bme.openehr.nodepath.dao;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openehr.am.archetype.Archetype;
import org.openehr.am.parser.ContentObject;
import org.openehr.am.parser.DADLParser;
import org.openehr.rm.binding.DADLBinding;
import org.openehr.rm.common.archetyped.Locatable;
import org.openehr.rm.support.identification.HierObjectID;
import org.openehr.rm.support.identification.UIDBasedID;
import org.springframework.stereotype.Component;

import se.acode.openehr.parser.ADLParser;
import edu.zju.bme.openehr.nodepath.model.CoarseNodePathEntity;
import edu.zju.bme.openehr.nodepath.model.CoarseNodePathIndex;
import edu.zju.bme.snippet.java.Reflector;

@Component("NodePathPersistenceDao")
public class NodePathPersistenceDaoImpl implements NodePathPersistenceDao {

	private Logger logger = Logger.getLogger(NodePathPersistenceDaoImpl.class.getName());

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;

    @Override
	public int insert(List<String> dadls, List<String> adls) {

		logger.info("insert");

		try {
			
			Map<String, Archetype> archetypes = new HashMap<>();
			for (String adl : adls) {
				ADLParser parser = new ADLParser(adl);
				Archetype archetype = parser.parse();
				archetypes.put(archetype.getArchetypeId().getValue(), archetype); 
			}

			Session s = sessionFactory.getCurrentSession();
			DADLBinding binding = new DADLBinding();

			for (String dadl : dadls) {
				logger.info(dadl);
				InputStream is = new ByteArrayInputStream(dadl.getBytes("UTF-8"));
				DADLParser parser = new DADLParser(is);
				ContentObject contentObj = parser.parse();
				Object archetypeInstance = binding.bind(contentObj);
				
				if (archetypeInstance instanceof Locatable) {
					Locatable loc = (Locatable) archetypeInstance;
					
					UIDBasedID uid = loc.getUid();
					if (loc.getUid() == null) {
						uid = new HierObjectID(UUID.randomUUID().toString());
						loc.setUid(uid);
					} else if (uid.getValue() == null || uid.getValue().isEmpty()) {
						uid.setValue(UUID.randomUUID().toString());
					}
					
					List<CoarseNodePathEntity> listCoarseNodePathEntity = 
							selectCoarseNodePathByObjectUids(Arrays.asList(uid.getValue()));
					CoarseNodePathEntity coarseNodePathEntity = new CoarseNodePathEntity();
					if (listCoarseNodePathEntity != null && !listCoarseNodePathEntity.isEmpty()) {
						coarseNodePathEntity = listCoarseNodePathEntity.get(0);
					}
					
					coarseNodePathEntity.setObjectUid(uid.getValue());
					coarseNodePathEntity.setArchetypeId(loc.getArchetypeNodeId());
					coarseNodePathEntity.setLastUpdateDateTime(new Date());
					coarseNodePathEntity.setDadl(binding.toDADLString(loc));
					s.save(coarseNodePathEntity);

					Query q = s.createQuery("delete from CoarseNodePathIndex as c where c.referenceId = :referenceId");
					q.setParameter("referenceId", coarseNodePathEntity.getId());
					q.executeUpdate();
					
					Archetype archetype = archetypes.get(loc.getArchetypeNodeId());
					Set<String> pathSet = archetype.getPathNodeMap().keySet();
					for (String path : pathSet) {
						persistNodePOJOFields(path, loc, coarseNodePathEntity.getId());
					}
				}
			}
			
		} catch (Exception e) {
			logger.error(e);
			return -2;
		}

		return 0;

	}
    
    @Override
	public int delete(String aql) {

		return delete(aql, null);

	}

	public int delete(String aql, Map<String, Object> parameters) {

		return executeUpdate(aql, parameters);

	}

	protected int executeUpdate(String aql, Map<String, Object> parameters) {

		logger.info("executeUpdate");

		logger.info(aql);

		try {

			Session s = sessionFactory.getCurrentSession();

			Query q = s.createQuery(aql);
			passParameters(q, parameters);
			int ret = q.executeUpdate();

			logger.info(ret);

			return ret;
		} catch (Exception e) {
			logger.error(e);
			return -2;
		}

	}

    @Override
	public List<CoarseNodePathEntity> select(String aql) {

		logger.info("select");

		try {

			long startTime = System.currentTimeMillis();

			Session s = sessionFactory.getCurrentSession();

			Query query = s.createQuery(aql);
			
			@SuppressWarnings("unchecked")
			List<CoarseNodePathIndex> listCoarseNodePathIndex = query.list();
			
			List<Integer> coarseNodePathEntityIds = new ArrayList<>();
			for (CoarseNodePathIndex coarseNodePathIndex : listCoarseNodePathIndex) {
				if (coarseNodePathEntityIds.contains(coarseNodePathIndex.getReferenceId())) {
					continue;
				}
				coarseNodePathEntityIds.add(coarseNodePathIndex.getReferenceId());
			}

			List<CoarseNodePathEntity> listCoarseNodePathEntity = selectCoarseNodePathByIds(coarseNodePathEntityIds);
			
			long endTime = System.currentTimeMillis();
			logger.info("aql execute time (ms) : " + (endTime - startTime));
			
			return listCoarseNodePathEntity;
			
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
    	
    }
    
    @Override
    public List<CoarseNodePathEntity> selectCoarseNodePathByObjectUids(List<String> objectUids) {

		logger.info("selectCoarseNodePathByObjectUids");

		try {
			
			String queryString = "from CoarseNodePathEntity as c where c.objectUid in :objectUid";

			Session s = sessionFactory.getCurrentSession();
			
			Query query = s.createQuery(queryString);
			query.setParameterList("objectUid", objectUids);
			
			@SuppressWarnings("unchecked")
			List<CoarseNodePathEntity> listCoarseNodePath = query.list();		
			
			return listCoarseNodePath;
			
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
    	
    }
    
    @Override
    public List<CoarseNodePathEntity> selectCoarseNodePathByIds(List<Integer> ids) {

		logger.info("selectCoarseNodePathByIds");

		try {
			
			String queryString = "from CoarseNodePathEntity as c where c.id in :id";

			Session s = sessionFactory.getCurrentSession();
			
			Query query = s.createQuery(queryString);
			query.setParameterList("id", ids);
			
			@SuppressWarnings("unchecked")
			List<CoarseNodePathEntity> listCoarseNodePath = query.list();		
			
			return listCoarseNodePath;
			
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
    	
    }
    
//    @Override
//    public List<CoarseNodePathEntity> selectCoarseNodePathByPathValues(Map<String, String> pathValues) {
//
//		logger.info("selectCoarseNodePathByPathValues");
//
//		try {
//			
//			String queryString = "from CoarseNodePathIndex as c where ";
//
//			Session s = sessionFactory.getCurrentSession();
//			
//			for (int i = 0; i < pathValues.size(); i++) {				
//				String conditionString ="(c.path = :path" + i + " and c.valueString = :value" + i + ")";
//				queryString += conditionString;
//				if (i < pathValues.size() - 1) {
//					queryString += " or ";						
//				}			
//			}
//
//			Query query = s.createQuery(queryString);
//			int i = 0;
//			for (String key : pathValues.keySet()) {
//				String value  = pathValues.get(key);				
//				query.setParameter("path" + i, key);
//				query.setParameter("value" + i, value);			
//				i++;
//			}
//			
//			@SuppressWarnings("unchecked")
//			List<CoarseNodePathIndex> listCoarseNodePathIndex = query.list();
//			
//			List<Integer> coarseNodePathEntityIds = new ArrayList<>();
//			for (CoarseNodePathIndex coarseNodePathIndex : listCoarseNodePathIndex) {
//				if (coarseNodePathEntityIds.contains(coarseNodePathIndex.getReferenceId())) {
//					continue;
//				}
//				coarseNodePathEntityIds.add(coarseNodePathIndex.getReferenceId());
//			}
//
//			List<CoarseNodePathEntity> listCoarseNodePathEntity = selectCoarseNodePathByIds(coarseNodePathEntityIds);
//			return listCoarseNodePathEntity;
//			
//		} catch (Exception e) {
//			logger.error(e);
//			return null;
//		}
//    	
//    }

    @Override
	public List<CoarseNodePathEntity> selectCoarseNodePathByPathValues(List<String> paths, List<String> values) {

		logger.info("selectCoarseNodePathByPathValues");

		try {

			long startTime = System.currentTimeMillis();
			
			String queryString = "from CoarseNodePathIndex as c where ";

			Session s = sessionFactory.getCurrentSession();
			
			for (int i = 0; i < paths.size(); i++) {				
				String conditionString ="(c.path = :path" + i + " and c.valueString = :value" + i + ")";
				queryString += conditionString;
				if (i < paths.size() - 1) {
					queryString += " or ";						
				}			
			}

			Query query = s.createQuery(queryString);
			for (int i = 0; i < paths.size(); i++) {			
				query.setParameter("path" + i, paths.get(i));
				query.setParameter("value" + i, values.get(i));	
				
			}
			
			@SuppressWarnings("unchecked")
			List<CoarseNodePathIndex> listCoarseNodePathIndex = query.list();
			
			List<Integer> coarseNodePathEntityIds = new ArrayList<>();
			for (CoarseNodePathIndex coarseNodePathIndex : listCoarseNodePathIndex) {
				if (coarseNodePathEntityIds.contains(coarseNodePathIndex.getReferenceId())) {
					continue;
				}
				coarseNodePathEntityIds.add(coarseNodePathIndex.getReferenceId());
			}

			List<CoarseNodePathEntity> listCoarseNodePathEntity = selectCoarseNodePathByIds(coarseNodePathEntityIds);
			
			long endTime = System.currentTimeMillis();
			logger.info("aql execute time (ms) : " + (endTime - startTime));
			
			return listCoarseNodePathEntity;
			
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
    	
    }

	protected void passParameters(Query q, Map<String, Object> parameters) {

		if (parameters != null) {
			for (String paraName : parameters.keySet()) {
				q.setParameter(paraName, parameters.get(paraName));
			}
		}

	}
	
	protected void persistNodePOJOFields(String nodePath, Locatable loc, int id) 
			throws IllegalArgumentException, IllegalAccessException {
		
		Object obj = loc.itemAtPath(nodePath);
		if (obj == null) {
			return;
		}
		
		Session s = sessionFactory.getCurrentSession();
		
		Iterable<Field> fields = Reflector.INSTANCE.getFieldsUpTo(obj.getClass(), null);
		for (Field field : fields) {
			if (field.getName().compareTo("ROOT") == 0 ||
					field.getName().compareTo("PATH_SEPARATOR") == 0) {
				continue;
			}
			
			field.setAccessible(true);
			String fieldPath = nodePath + "/" + field.getName();
			CoarseNodePathIndex coarseNodePathIndex = new CoarseNodePathIndex();
			coarseNodePathIndex.setReferenceId(id);
			coarseNodePathIndex.setPath(fieldPath);
			if (field.getType() == String.class || 
					field.getType() == Integer.class || 
					field.getType() == Double.class || 
					field.getType() == Date.class || 
					field.getType() == Boolean.class) {
				if (field.get(obj) == null) {
					continue;
				} else {
					coarseNodePathIndex.setValueString(field.get(obj).toString());
				}		
			} else {
				continue;
			}
			s.save(coarseNodePathIndex);			
		}
		
	}

}
