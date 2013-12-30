package edu.zju.bme.openehr.nodepath.service;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import org.springframework.stereotype.Component;

import edu.zju.bme.openehr.nodepath.dao.NodePathPersistenceDao;
import edu.zju.bme.openehr.nodepath.model.CoarseNodePath;
import edu.zju.bme.openehr.nodepath.model.FineNodePath;

@WebService(endpointInterface = "edu.zju.bme.openehr.nodepath.service.NodePathPersistence")
@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
@Component("NodePathPersistence")
public class NodePathPersistenceImpl implements NodePathPersistence {
	
	private NodePathPersistenceDao nodePathPersistenceDao;
    
	@Resource(name="NodePathPersistenceDao")
	public void setProductTypeDao(NodePathPersistenceDao nodePathPersistenceDao) {
		this.nodePathPersistenceDao = nodePathPersistenceDao;
	}

	@Override
	public int insert(String[] dadls) {
		
		return nodePathPersistenceDao.insert(dadls);

	}

	@Override
	public CoarseNodePath[] selectCoarseNodePathByObjectUids(String[] objectUids) {
		
		return nodePathPersistenceDao.selectCoarseNodePathByObjectUids(objectUids);

	}

	@Override
	public FineNodePath[] selectFineNodePathByObjectUids(String[] objectUids) {
		
		return nodePathPersistenceDao.selectFineNodePathByObjectUids(objectUids);

	}

}
