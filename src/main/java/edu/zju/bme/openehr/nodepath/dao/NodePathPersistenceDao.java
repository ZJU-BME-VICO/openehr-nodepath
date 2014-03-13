package edu.zju.bme.openehr.nodepath.dao;

import java.util.List;
import java.util.Map;

import edu.zju.bme.openehr.nodepath.model.CoarseNodePathEntity;

public interface NodePathPersistenceDao {

	int insert(List<String> dadls, List<String> adls);

	int delete(String adl);
	
	List<CoarseNodePathEntity> select(String aql);
	
	List<CoarseNodePathEntity> selectCoarseNodePathByObjectUids(List<String> objectUids);

	List<CoarseNodePathEntity> selectCoarseNodePathByIds(List<Integer> ids);
	
//	List<CoarseNodePathEntity> selectCoarseNodePathByPathValues(Map<String, String> pathValues);

	List<CoarseNodePathEntity> selectCoarseNodePathByPathValues(List<String> paths, List<String> values);

}
