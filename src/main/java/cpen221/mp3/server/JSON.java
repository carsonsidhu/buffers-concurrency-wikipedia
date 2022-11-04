package cpen221.mp3.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A suite of static methods for parsing and assembling JSON expressions including key, value, and list expressions
 */
public class JSON {

    /**
     * Parses a JSON String into a map of key,value pairs
     *
     * @param jsonInput A JSON formatted string
     * @return A <String,String> map of key value pairs</String,String>
     */
    public static Map<String, String> parseJSON(String jsonInput) {
        String[] entries = jsonInput.replaceAll("[{}\"]", "").split("[,]");
        Map<String, String> output = new HashMap<>();

        for (String temp : entries) {
            String[] keyValueSplit = temp.split("[:]");
            output.put(keyValueSplit[0].trim(), keyValueSplit[1].trim());
        }
        return output;
    }

    /**
     * Formats a key and list into JSON
     *
     * @param key  a key
     * @param list a list
     * @return A JSON formatted String containing the key and list. Does not include trailing comma or {}
     */
    public static String formatList(String key, List<String> list) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("\"" + key + "\": [");
        for (String temp : list) {
            jsonBuilder.append("\"" + temp + "\", ");
        }
        jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
        jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }

    /**
     * Formats a key and value into JSON
     *
     * @param key   a key
     * @param value a value
     * @return A JSON formatted String containing the key and value. Does not include trailing comma or {}
     */
    public static String formatValue(String key, String value) {
        return "\"" + key + "\": \"" + value + "\"";
    }

    /**
     * Takes an array of formatted JSON Strings and formats them with delimiting commas and {}
     *
     * @param pairs formatted JSON lines
     * @return A JSON formatted expression containing all input pairs
     */
    public static String assembleExpression(String[] pairs) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{ ");
        for (String temp : pairs) {
            jsonBuilder.append(temp);
            jsonBuilder.append(", ");
        }
        jsonBuilder.deleteCharAt(jsonBuilder.length() - 2);
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }
}
