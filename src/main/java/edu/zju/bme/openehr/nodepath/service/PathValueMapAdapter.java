package edu.zju.bme.openehr.nodepath.service;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class PathValueMapAdapter extends XmlAdapter<PathValueMap, Map<String, String>> {

    @Override  
    public PathValueMap marshal(Map<String, String> v) throws Exception {
    	PathValueMap map = new PathValueMap();
        for (Map.Entry<String, String> e : v.entrySet()) {
            map.getEntries().add(new PathValueMap.PathValueEntry(e.getKey(), e.getValue()));
        }
        return map;
    }

    @Override  
    public Map<String, String> unmarshal(PathValueMap v) throws Exception {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (PathValueMap.PathValueEntry e : v.getEntries()) {
            map.put(e.getPath(), e.getValue());
        }
        return map;
    }

}
