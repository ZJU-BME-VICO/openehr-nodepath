package edu.zju.bme.openehr.nodepath.service;

import java.util.List;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import edu.zju.bme.openehr.nodepath.model.CoarseNodePathEntity;

@WebService
public interface NodePathPersistence {

	@WebMethod
	int insert(List<String> dadls, List<String> adls);

	@WebMethod
	int delete(String adl);

	@WebMethod
	List<CoarseNodePathEntity> select(String aql);

	@WebMethod
	List<CoarseNodePathEntity> selectCoarseNodePathByObjectUids(List<String> objectUids);

//	@WebMethod
//	List<CoarseNodePathEntity> selectCoarseNodePathByPathValues(
//			@XmlJavaTypeAdapter(PathValueMapAdapter.class) Map<String, String> pathValues);

	@WebMethod
	List<CoarseNodePathEntity> selectCoarseNodePathByPathValues(List<String> paths, List<String> values);

}
