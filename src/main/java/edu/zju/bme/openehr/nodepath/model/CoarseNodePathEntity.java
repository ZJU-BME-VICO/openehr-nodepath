package edu.zju.bme.openehr.nodepath.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class CoarseNodePathEntity {

	private Integer id;
	private String objectUid;
	private String archetypeId;
	private String dadl;
	private Date lastUpdateDateTime;

	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(unique = true)
	public String getObjectUid() {
		return objectUid;
	}
	
	public void setObjectUid(String objectUid) {
		this.objectUid = objectUid;
	}

	public String getArchetypeId() {
		return archetypeId;
	}

	public void setArchetypeId(String archetypeId) {
		this.archetypeId = archetypeId;
	}

	@Lob
	public String getDadl() {
		return dadl;
	}
	
	public void setDadl(String dadl) {
		this.dadl = dadl;
	}
	
	public Date getLastUpdateDateTime() {
		return lastUpdateDateTime;
	}

	public void setLastUpdateDateTime(Date lastUpdateDateTime) {
		this.lastUpdateDateTime = lastUpdateDateTime;
	}
	
}
