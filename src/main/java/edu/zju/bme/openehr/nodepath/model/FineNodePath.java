package edu.zju.bme.openehr.nodepath.model;

import java.sql.Types;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class FineNodePath {

	private Integer id;
	private String objectUid;
	private String archetypeId;
	private String path;
	private Integer valueType;
	private String valueString;
	private Integer valueInteger;
	private Double valueDouble;
	private Date valueDate;

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
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public Integer getValueType() {
		return valueType;
	}
	
	public void setValueType(Integer valueType) {
		this.valueType = valueType;
	}
	
	public String getValueString() {
		return valueString;
	}
	
	public void setValueString(String valueString) {
		this.valueString = valueString;
	}
	
	public Integer getvalueInteger() {
		return valueInteger;
	}
	
	public void setvalueInteger(Integer valueInteger) {
		this.valueInteger = valueInteger;
	}
	
	public Double getValueDouble() {
		return valueDouble;
	}
	
	public void setValueDouble(Double valueDouble) {
		this.valueDouble = valueDouble;
	}
	
	public Date getValueDate() {
		return valueDate;
	}
	
	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}
	
	@Transient
	public Object getValue() {
		switch (this.valueType) {
		case Types.INTEGER:
			return this.getvalueInteger();
		case Types.DOUBLE:
			return this.getValueDouble();
		case Types.DATE:
			return this.getValueDate();
		case Types.NVARCHAR:
			return this.getValueString();
		default:
			return null;
		}
	}

}
