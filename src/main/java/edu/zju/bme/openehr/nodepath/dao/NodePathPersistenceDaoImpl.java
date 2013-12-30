package edu.zju.bme.openehr.nodepath.dao;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openehr.am.parser.ContentObject;
import org.openehr.am.parser.DADLParser;
import org.openehr.rm.binding.DADLBinding;
import org.openehr.rm.common.archetyped.Locatable;
import org.springframework.stereotype.Component;

import edu.zju.bme.openehr.nodepath.model.CoarseNodePath;
import edu.zju.bme.openehr.nodepath.model.FineNodePath;

@Component("NodePathPersistenceDao")
public class NodePathPersistenceDaoImpl implements NodePathPersistenceDao {

	private Logger logger = Logger.getLogger(NodePathPersistenceDaoImpl.class.getName());

	private SessionFactory sessionFactory;
    
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

    @Override
	public int insert(String[] dadls) {

		logger.info("insert");

		try {

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
					
					CoarseNodePath coarseNodePath = new CoarseNodePath();
					coarseNodePath.setObjectUid(loc.getUid().getValue());
					coarseNodePath.setArchetypeId(loc.getArchetypeNodeId());
					coarseNodePath.setDadl(binding.toDADLString(loc));
					s.save(coarseNodePath);
				}
			}
			
		} catch (Exception e) {
			logger.error(e);
			return -2;
		}

		return 0;

	}
    
    @Override
    public CoarseNodePath[] selectCoarseNodePathByObjectUids(String[] objectUids) {

		logger.info("selectCoarseNodePathByObjectIds");

		try {

			Session s = sessionFactory.getCurrentSession();
			
			String[] attributeNames = new String[] {"objectUid"};
			String queryString = buildSelectString("CoarseNodePath", attributeNames, 1);
			
			Query query = s.createQuery(queryString);
			query.setParameterList("objectUid", objectUids);
			
			@SuppressWarnings("unchecked")
			List<CoarseNodePath> listCoarseNodePath = query.list();		
			
			return listCoarseNodePath.toArray(new CoarseNodePath[] {});
			
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
    	
    }
    
    @Override
    public FineNodePath[] selectFineNodePathByObjectUids(String[] objectUids) {

		logger.info("selectFineNodePathByObjectUids");

		try {

			Session s = sessionFactory.getCurrentSession();
			
			String[] attributeNames = new String[] {"objectUid"};
			String queryString = buildSelectString("FineNodePath", attributeNames, 1);
			
			Query query = s.createQuery(queryString);
			query.setParameterList("objectUid", objectUids);
			
			@SuppressWarnings("unchecked")
			List<FineNodePath> listFineNodePath = query.list();		
			
			return listFineNodePath.toArray(new FineNodePath[] {});
			
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
    	
    }
    
    protected String buildSelectString(String className, String[] attributeNames, int relation) {

		String queryString = "from " + className + " as c";
		
    	if (attributeNames != null) {
    		queryString += " where ";
        	
    		String inString = "";
    		for (int i = 0; i < attributeNames.length; i++) {
    			
    			inString += "(c." + attributeNames[i] + " in(:" + attributeNames[i] + "))";
    			if (i < attributeNames.length - 1) {
    				if (relation == 1) {
    					inString += " and ";
					} else {
    					inString += " or ";						
					}
				}
    		}
    		
    		queryString += inString;
		}
		
		logger.info(queryString);	
    	
    	return queryString;
		
	}

}
