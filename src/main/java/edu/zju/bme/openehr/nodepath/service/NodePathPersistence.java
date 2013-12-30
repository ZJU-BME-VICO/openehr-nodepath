package edu.zju.bme.openehr.nodepath.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

import edu.zju.bme.openehr.nodepath.model.CoarseNodePath;
import edu.zju.bme.openehr.nodepath.model.FineNodePath;

@WebService
public interface NodePathPersistence {

	@WebMethod
	int insert(String[] dadls);

	@WebMethod
	CoarseNodePath[] selectCoarseNodePathByObjectUids(String[] objectUids);

	@WebMethod
	FineNodePath[] selectFineNodePathByObjectUids(String[] objectUids);

}
