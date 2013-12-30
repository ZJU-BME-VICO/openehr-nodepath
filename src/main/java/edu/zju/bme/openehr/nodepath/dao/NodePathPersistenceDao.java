package edu.zju.bme.openehr.nodepath.dao;

import edu.zju.bme.openehr.nodepath.model.CoarseNodePath;
import edu.zju.bme.openehr.nodepath.model.FineNodePath;

public interface NodePathPersistenceDao {

	int insert(String[] dadls);
	
	CoarseNodePath[] selectCoarseNodePathByObjectUids(String[] objectUids);
	
	FineNodePath[] selectFineNodePathByObjectUids(String[] objectUids);

}
