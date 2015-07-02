package classes.metier;

import java.util.HashMap;

public class TypeIncidentDB {
    private long id;
    private HashMap<String,String> strings;
    private HashMap<String,Integer> integers;

    public TypeIncidentDB()
    {
        this.strings = new HashMap<String,String>();
        this.integers = new HashMap<String,Integer>();
        this.id = -1;
    }

    public TypeIncidentDB setInt(String key, int value)
    {
        this.integers.put(key, value);
        return this;
    }

    public TypeIncidentDB setString(String key, String value)
    {
        this.strings.put(key, value);
        return this;
    }

    public TypeIncidentDB set(String key, Object value)
    {
        if (value instanceof String)
        {
            this.strings.put(key, (String)value);
        }
        else if (value instanceof Integer)
        {
            this.integers.put(key, (Integer) value);
        }

        return this;
    }

    public Integer getInt(String key)
    {
        return this.integers.get(key);
    }

    public String getString(String key)
    {

        return this.strings.get(key);
    }

    public Object get(String key)
    {
        if (this.integers.containsKey(key))
        {
            return this.integers.get(key);
        }
        else if (this.strings.containsKey(key))
        {
            return this.strings.get(key);
        }

        return null;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getId()
    {
        return this.id;
    }

    public boolean contains(String key)
    {
        if (this.integers.containsKey(key))
        {
            return true;
        }
        else if (this.strings.containsKey(key))
        {
            return true;
        }

        return false;
    }

    @Override
    public String toString()
    {
        return "TypeIncident";
    }
}