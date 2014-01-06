package edu.zju.bme.openehr.nodepath.service;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "PathValueMap")
@XmlAccessorType(XmlAccessType.FIELD)
public class PathValueMap {
	
    @XmlElement(nillable = false, name = "entry")
    List<PathValueEntry> entries = new ArrayList<PathValueEntry>();

    public List<PathValueEntry> getEntries() {
        return entries;
    }

    @XmlType(name = "PathValueEntry")
    @XmlAccessorType(XmlAccessType.FIELD)
    static class PathValueEntry {

		@XmlElement(required = true, nillable = false)
        private String path;
		private String value;

		public PathValueEntry() {
		}
    	
        public PathValueEntry(String path, String value) {
			super();
			this.path = path;
			this.value = value;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
		
    }

}
