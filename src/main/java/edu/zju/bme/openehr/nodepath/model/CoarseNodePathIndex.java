package edu.zju.bme.openehr.nodepath.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class CoarseNodePathIndex {

	private Integer id;
	private Integer referenceId;
	private String archetypeId;
	private String path;
	private Integer valueType;
	private String valueString;
	private Integer valueInteger;
	private Double valueDouble;
	private Date valueDate;
	private Boolean valueBoolean;

	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
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

	@Lob
	public String getValueString() {
		return valueString;
	}

	public void setValueString(String valueString) {
		this.valueString = valueString;
	}

	public Integer getValueInteger() {
		return valueInteger;
	}

	public void setValueInteger(Integer valueInteger) {
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

	public Boolean getValueBoolean() {
		return valueBoolean;
	}

	public void setValueBoolean(Boolean valueBoolean) {
		this.valueBoolean = valueBoolean;
	}
	
}
