package fr.epsi.helper;

import java.util.HashMap;
import java.util.Map;

public class VarsGlobals {

    private static Map<String,Object> values = new HashMap<String, Object>();
    
    public static Object get (String key)
    {
    	return values.get(key);
    }
    
    public static void set (String key, Object object)
    {
    	values.put(key, object);
    }

}
