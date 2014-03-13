package edu.zju.bme.openehr.nodepath.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.zju.bme.openehr.nodepath.dao.NodePathPersistenceDao;
import edu.zju.bme.openehr.nodepath.model.CoarseNodePathEntity;

@WebService(endpointInterface = "edu.zju.bme.openehr.nodepath.service.NodePathPersistence")
@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
@Component("NodePathPersistence")
@Transactional
public class NodePathPersistenceImpl implements NodePathPersistence {

	@Resource(name="NodePathPersistenceDao")
	private NodePathPersistenceDao nodePathPersistenceDao;

	@Override
	public int insert(List<String> dadls, List<String> adls) {
		
		return nodePathPersistenceDao.insert(dadls, adls);

	}

	@Override
	public int delete(String aql) {
		
		return nodePathPersistenceDao.delete(aql);

	}

	@Override
	public List<CoarseNodePathEntity> select(String aql) {
		
		return nodePathPersistenceDao.select(aql);

	}

	@Override
	public List<CoarseNodePathEntity> selectCoarseNodePathByObjectUids(List<String> objectUids) {
		
		return nodePathPersistenceDao.selectCoarseNodePathByObjectUids(objectUids);

	}

//	@Override
//	public List<CoarseNodePathEntity> selectCoarseNodePathByPathValues(Map<String, String> pathValues) {
//		
//		return nodePathPersistenceDao.selectCoarseNodePathByPathValues(pathValues);
//
//	}

	@Override
	public List<CoarseNodePathEntity> selectCoarseNodePathByPathValues(List<String> paths, List<String> values) {
		
		return nodePathPersistenceDao.selectCoarseNodePathByPathValues(paths, values);

	}

}
