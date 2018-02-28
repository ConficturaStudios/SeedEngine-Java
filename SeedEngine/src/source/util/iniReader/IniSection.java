package source.util.iniReader;

import java.util.HashMap;
import java.util.Map;

/**
 * A section of .ini file data
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
class IniSection {
    /** A map of keys and the identifier of the map the key is stored in */
    private Map<String, Integer> keys = new HashMap<>();
    /** A map of Float values */
    private Map<String, Float> floatMap = new HashMap<>(); //<name, value> : 0
    /** A map of Float List values */
    private Map<String, Float[]> floatListMap = new HashMap<>(); //<name, value> : 1
    /** A map of Boolean values */
    private Map<String, Boolean> booleanMap = new HashMap<>(); //<name, value> : 2
    /** A map of Boolean List values */
    private Map<String, Boolean[]> booleanListMap = new HashMap<>(); //<name, value> : 3
    /** A map of String values */
    private Map<String, String> stringMap = new HashMap<>(); //<name, value> : 4
    /** A map of String List values */
    private Map<String, String[]> stringListMap = new HashMap<>(); //<name, value> : 5

    /**
     * Adds the value to the collection of values
     * @param key Key to add
     * @param value Value to add
     * @throws IniException if key already exists
     */
    void addFloat(String key, Float value) throws IniException {
        if (this.keys.keySet().contains(key)) {
            throw new IniException("Key already written");
        }
        this.floatMap.put(key, value);
        this.keys.put(key, 0);
    }

    /**
     * Adds the value to the collection of values
     * @param key Key to add
     * @param value Value to add
     * @throws IniException if key already exists
     */
    void addFloatList(String key, Float[] value) throws IniException {
        if (this.keys.keySet().contains(key)) {
            throw new IniException("Key already written");
        }
        this.floatListMap.put(key, value);
        this.keys.put(key, 1);
    }

    /**
     * Adds the value to the collection of values
     * @param key Key to add
     * @param value Value to add
     * @throws IniException if key already exists
     */
    void addBoolean(String key, Boolean value) throws IniException {
        if (this.keys.keySet().contains(key)) {
            throw new IniException("Key already written");
        }
        this.booleanMap.put(key, value);
        this.keys.put(key, 2);
    }

    /**
     * Adds the value to the collection of values
     * @param key Key to add
     * @param value Value to add
     * @throws IniException if key already exists
     */
    void addBooleanList(String key, Boolean[] value) throws IniException {
        if (this.keys.keySet().contains(key)) {
            throw new IniException("Key already written");
        }
        this.booleanListMap.put(key, value);
        this.keys.put(key, 3);
    }

    /**
     * Adds the value to the collection of values
     * @param key Key to add
     * @param value Value to add
     * @throws IniException if key already exists
     */
    void addString(String key, String value) throws IniException {
        if (this.keys.keySet().contains(key)) {
            throw new IniException("Key already written");
        }
        this.stringMap.put(key, value);
        this.keys.put(key, 4);
    }

    /**
     * Adds the value to the collection of values
     * @param key Key to add
     * @param value Value to add
     * @throws IniException if key already exists
     */
    void addStringList(String key, String[] value) throws IniException {
        if (this.keys.keySet().contains(key)) {
            throw new IniException("Key already written");
        }
        this.stringListMap.put(key, value);
        this.keys.put(key, 5);
    }

    /**
     * Gets the specified object by key
     * @param key key to search
     * @return stored object, or null if not found
     * @throws IllegalArgumentException if key is invalid
     */
    private Object get(String key) throws IllegalArgumentException {
        Integer collection = keys.get(key);
        if (collection == null) {
            throw new IllegalArgumentException("Invalid key");
        }
        switch (collection) {
            case 0:
                return floatMap.get(key);
            case 1:
                return floatListMap.get(key);
            case 2:
                return booleanMap.get(key);
            case 3:
                return booleanListMap.get(key);
            case 4:
                return stringMap.get(key);
            case 5:
                return stringListMap.get(key);
        }
        return null;
    }

    /**
     * Gets the float value from the specified key
     * @param key key to search
     * @return float value
     * @throws IllegalArgumentException if key is invalid
     */
    Float getFloat(String key) throws IllegalArgumentException{
        try {
            Object getResult = get(key);
            if (getResult instanceof Float) {
                return (Float)getResult;
            } else {
                throw new IllegalArgumentException("Invalid key");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Gets the float[] value from the specified key
     * @param key key to search
     * @return float[] value
     * @throws IllegalArgumentException if key is invalid
     */
    Float[] getFloatList(String key) throws IllegalArgumentException{
        try {
            Object getResult = get(key);
            if (getResult instanceof Float[]) {
                return (Float[])getResult;
            } else {
                throw new IllegalArgumentException("Invalid key");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Gets the boolean value from the specified key
     * @param key key to search
     * @return boolean value
     * @throws IllegalArgumentException if key is invalid
     */
    Boolean getBoolean(String key) throws IllegalArgumentException{
        try {
            Object getResult = get(key);
            if (getResult instanceof Boolean) {
                return (Boolean)getResult;
            } else {
                throw new IllegalArgumentException("Invalid key");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Gets the boolean[] value from the specified key
     * @param key key to search
     * @return boolean[] value
     * @throws IllegalArgumentException if key is invalid
     */
    Boolean[] getBooleanList(String key) throws IllegalArgumentException{
        try {
            Object getResult = get(key);
            if (getResult instanceof Boolean[]) {
                return (Boolean[])getResult;
            } else {
                throw new IllegalArgumentException("Invalid key");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Gets the String value from the specified key
     * @param key key to search
     * @return String value
     * @throws IllegalArgumentException if key is invalid
     */
    String getString(String key) throws IllegalArgumentException{
        try {
            Object getResult = get(key);
            if (getResult instanceof String) {
                return (String)getResult;
            } else {
                throw new IllegalArgumentException("Invalid key");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Gets the String[] value from the specified key
     * @param key key to search
     * @return String[] value
     * @throws IllegalArgumentException if key is invalid
     */
    String[] getStringList(String key) throws IllegalArgumentException{
        try {
            Object getResult = get(key);
            if (getResult instanceof String[]) {
                return (String[])getResult;
            } else {
                throw new IllegalArgumentException("Invalid key");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}
