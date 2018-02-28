package source.util.iniReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Parses a passed .ini file into a map of key value pairs
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
class IniReader {
    /**
     * Reads an ini file into a map of section data (Section name, IniSection)
     * @param fileName file to read
     * @return .ini section map
     * @throws IniException if read error encountered
     * @since Version 0.1
     */
    static Map<String, IniSection> read(String fileName) throws IniException {
        Map<String, IniSection> ret = new HashMap<>();
        File file = new File(fileName);
        try {
            Scanner fileScan = new Scanner(file);
            String current = "[]";
            ret.put("[]", new IniSection());
            while (fileScan.hasNextLine()) {
                String next = fileScan.nextLine();
                if (next.equals("")) continue;
                //Separate out comments
                String data;
                int commentIndex = next.replaceAll("\\s+", "").indexOf(';');
                if (commentIndex < 0) {
                    data = next;
                } else if (commentIndex == 0) {
                    continue;
                } else {
                    data = next.substring(0, next.indexOf(';'));
                }

                //Parse data
                int equalsIndex = data.indexOf('=');
                int lBracketIndex = data.indexOf('[');
                int rBracketIndex = data.indexOf(']');
                if (equalsIndex < 0) {
                    if (lBracketIndex == 0 && rBracketIndex > 1) {
                        String c = data.substring(lBracketIndex + 1, rBracketIndex);
                        if (ret.keySet().contains(c)) {
                            throw new IniException();
                        }
                        current = c;
                        ret.put(current, new IniSection());
                    } else {
                        throw new IniException();
                    }
                } else if (equalsIndex == 0) {
                    throw new IniException();
                } else {
                    String[] keyValue = data.split("=");
                    if (keyValue.length != 2) {
                        throw new IniException();
                    } else {
                        String key = keyValue[0].replaceAll("\\s+", "");
                        String value = keyValue[1];
                        if (value.indexOf("{") > 0 && value.indexOf("}") > value.indexOf("{")) {
                            String[] values = value.substring(
                                    value.indexOf("{") + 1, value.indexOf("}")
                            ).split(",");
                            try {
                                Float[] f = new Float[values.length];
                                f[0] = Float.parseFloat(values[0].replaceAll("\\s+", ""));
                                for (int i = 0; i < f.length; i++) {
                                    f[i] = Float.parseFloat(values[i].replaceAll("\\s+", ""));
                                }
                                ret.get(current).addFloatList(key, f);
                            } catch (NumberFormatException e) {
                                if (values[0].toLowerCase().replaceAll("\\s+", "").equals("true") ||
                                        values[0].toLowerCase().replaceAll("\\s+", "").equals("false")) {
                                    Boolean[] b = new Boolean[values.length];
                                    boolean complete = true;
                                    for (int i = 0; i < b.length; i++) {
                                        if (value.toLowerCase().replaceAll("\\s+", "").equals("true")) {
                                            b[i] = true;
                                        } else if (value.toLowerCase().replaceAll("\\s+", "").equals("false")) {
                                            b[i] = false;
                                        } else {
                                            ret.get(current).addStringList(key, values);
                                            complete = false;
                                            break;
                                        }
                                    }
                                    if (complete) {
                                        ret.get(current).addBooleanList(key, b);
                                    }
                                } else {
                                    ret.get(current).addStringList(key, values);
                                }

                            }
                        } else {
                            try {
                                Float f = Float.parseFloat(value.replaceAll("\\s+", ""));
                                ret.get(current).addFloat(key, f);
                            } catch (NumberFormatException e) {
                                if (value.toLowerCase().replaceAll("\\s+", "").equals("true")) {
                                    ret.get(current).addBoolean(key, true);
                                } else if (value.toLowerCase().replaceAll("\\s+", "").equals("false")) {
                                    ret.get(current).addBoolean(key, false);
                                } else {
                                    ret.get(current).addString(key, value);
                                }
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            return null;
        }
        return ret;
    }

}
