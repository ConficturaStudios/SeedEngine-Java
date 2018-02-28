package source.util.iniReader;

import java.util.HashMap;
import java.util.Map;

/**
 * A collection of IniSections to be output by the IniReader
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class IniFile {
    /** All sections of .ini data */
    private Map<String, IniSection> sectionMap = new HashMap<>(); //<name, section>
    /** The name of the file this data is from */
    private final String fileName;

    /**
     * IniFile Constructor
     * @param fileName .ini file name
     * @throws IllegalArgumentException if file name is invalid
     */
    public IniFile(String fileName) throws IllegalArgumentException {
        this.fileName = fileName;
        try {
            Map<String, IniSection> sections = IniReader.read(this.fileName);
            if (sections != null) {
                if (sections.size() > 0) {
                    for (String key : sections.keySet()) {
                        addSection(key, sections.get(key));
                    }
                }
            }
        } catch (IniException e) {
            throw new IllegalArgumentException("Invalid ini file");
        }

    }

    /**
     * Returns the name of the file referenced by this data
     * @return fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Adds a section to the sectionMap
     * @param name name of the section
     * @param section section data to add
     * @throws IllegalArgumentException if name is already used or section is null
     */
    private void addSection(String name, IniSection section) throws IllegalArgumentException {
        if (sectionMap.keySet().contains(name)) {
            throw new IllegalArgumentException("Section already exists");
        }
        if (section == null) {
            throw new IllegalArgumentException("Section cannot be null");
        }
        sectionMap.put(name, section);
    }

    /**
     * Retrieves a Float from the specified section at key
     * @param section section to search
     * @param key key to search
     * @return Float value
     * @throws IllegalArgumentException if section or key is invalid
     */
    public float getFloat(String section, String key) throws IllegalArgumentException {
        IniSection inisection = sectionMap.get(section);
        if (inisection == null) throw new IllegalArgumentException("Invalid Section");
        try {
            return inisection.getFloat(key);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Retrieves a Float list from the specified section at key
     * @param section section to search
     * @param key key to search
     * @return Float list value
     * @throws IllegalArgumentException if section or key is invalid
     */
    public Float[] getFloatList(String section, String key) throws IllegalArgumentException {
        IniSection inisection = sectionMap.get(section);
        if (inisection == null) throw new IllegalArgumentException("Invalid Section");
        try {
            return inisection.getFloatList(key);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Retrieves a Boolean from the specified section at key
     * @param section section to search
     * @param key key to search
     * @return Boolean value
     * @throws IllegalArgumentException if section or key is invalid
     */
    public boolean getBoolean(String section, String key) throws IllegalArgumentException {
        IniSection inisection = sectionMap.get(section);
        if (inisection == null) throw new IllegalArgumentException("Invalid Section");
        try {
            return inisection.getBoolean(key);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Retrieves a Boolean list from the specified section at key
     * @param section section to search
     * @param key key to search
     * @return Boolean list value
     * @throws IllegalArgumentException if section or key is invalid
     */
    public Boolean[] getBooleanList(String section, String key) throws IllegalArgumentException {
        IniSection inisection = sectionMap.get(section);
        if (inisection == null) throw new IllegalArgumentException("Invalid Section");
        try {
            return inisection.getBooleanList(key);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Retrieves a float from the specified section at key
     * @param section section to search
     * @param key key to search
     * @return float value
     * @throws IllegalArgumentException if section or key is invalid
     */
    public String getString(String section, String key) throws IllegalArgumentException {
        IniSection inisection = sectionMap.get(section);
        if (inisection == null) throw new IllegalArgumentException("Invalid Section");
        try {
            return inisection.getString(key);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Retrieves a float list from the specified section at key
     * @param section section to search
     * @param key key to search
     * @return float list value
     * @throws IllegalArgumentException if section or key is invalid
     */
    public String[] getStringList(String section, String key) throws IllegalArgumentException {
        IniSection inisection = sectionMap.get(section);
        if (inisection == null) throw new IllegalArgumentException("Invalid Section");
        try {
            return inisection.getStringList(key);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    
}
