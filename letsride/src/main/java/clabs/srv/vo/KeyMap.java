package clabs.srv.vo;

import org.apache.commons.collections.map.ListOrderedMap;

import clabs.tools.StringUtils;

public class KeyMap extends ListOrderedMap {
	
	private static final long serialVersionUID = -6585063284435943085L;

	public Object put(Object key, Object value) {
//		System.out.println("key:" + key +": value=" + value + "/" + StringUtils.toCamelCase((String) key));
        return super.put(StringUtils.toCamelCase((String) key), value);
    }
}
