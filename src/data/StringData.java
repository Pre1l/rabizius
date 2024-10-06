package data;

import java.util.HashMap;

/**
 * Saves/manages a string, string hashmap
 */
class StringData {
    private HashMap<String, String> dataMap;

    public StringData() {
        this.dataMap = new HashMap<>();
    }

    public void setData(String key, String value) {
        dataMap.put(key, value);
    }

    public String getData(String key) {
        String value = dataMap.get(key);

        return value;
    }

    public HashMap<String, String> getDataMap() {
        return dataMap;
    }
}
