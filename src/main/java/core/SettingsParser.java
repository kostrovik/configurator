package core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * project: configurator
 * author:  kostrovik
 * date:    05/07/2018
 * github:  https://github.com/kostrovik/configurator
 */
public class SettingsParser {
    private static Logger logger = LogManager.getLogger(SettingsParser.class);
    private Properties properties;
    private Map<String, Object> config;

    public SettingsParser(Properties properties) {
        this.properties = properties;
        config = new ConcurrentHashMap<>();
    }

    private void prepareConfig() {
        logger.info("Подготовка конфигурации");
        for (Object key : properties.keySet()) {
            prepareProperty((String) key);
        }
    }

    private List<String> parseKey(String key) {
        return Arrays.asList(key.split("\\."));
    }

    private void prepareProperty(String key) {
        String value = properties.getProperty(key);
        List<String> parsedKey = parseKey(key);

        if (!config.containsKey(parsedKey.get(0))) {
            setKey(parsedKey, value);
        } else {
            findAndSetKey(parsedKey, value);
        }
    }

    private void findAndSetKey(List<String> keys, String value) {
        List<String> mapKeys = new ArrayList<>(keys);
        Object configValue = config.get(mapKeys.get(0));
        mapKeys.remove(0);

        while (mapKeys.size() > 0) {
            if (configValue instanceof Map) {
                if (((Map) configValue).containsKey(mapKeys.get(0))) {
                    configValue = ((Map) configValue).get(mapKeys.get(0));
                    mapKeys.remove(0);
                } else {
                    ((Map) configValue).put(mapKeys.get(0), createValueMap(mapKeys, value));
                    mapKeys.clear();
                }
            }
        }

    }

    private Object createValueMap(List<String> keys, String value) {
        if (keys.size() > 1) {
            List<String> mapKeys = new ArrayList<>(keys);
            String lastKey = mapKeys.get(mapKeys.size() - 1);

            Map<String, Object> beforValue = new ConcurrentHashMap<>();
            beforValue.put(lastKey, value);
            mapKeys.remove(keys.size() - 1);
            for (int i = mapKeys.size() - 1; i > 0; i--) {
                Map<String, Object> newValue = new ConcurrentHashMap<>();
                newValue.put(mapKeys.get(i), beforValue);
                beforValue = newValue;
            }

            return beforValue;

        } else {
            return value;
        }
    }

    private void setKey(List<String> keys, String value) {
        if (keys.size() > 1) {
            List<String> mapKeys = new ArrayList<>(keys);
            String lastKey = mapKeys.get(mapKeys.size() - 1);

            Map<String, Object> beforValue = new ConcurrentHashMap<>();
            beforValue.put(lastKey, value);
            mapKeys.remove(keys.size() - 1);
            for (int i = mapKeys.size() - 1; i > 0; i--) {
                Map<String, Object> newValue = new ConcurrentHashMap<>();
                newValue.put(mapKeys.get(i), beforValue);
                beforValue = newValue;
            }

            config.put(keys.get(0), beforValue);

        } else {
            config.put(keys.get(0), value);
        }
    }

    public Map getConfig() {
        prepareConfig();
        return config;
    }

    public Object getConfigProperty(String property) {
        return findProperty(property, config);
    }

    private Object findProperty(String property, Map properties) {
        if (properties.keySet().contains(property)) {
            return properties.get(property);
        }

        for (Object o : properties.values()) {
            if (o instanceof Map) {
                return findProperty(property, (Map) o);
            }
        }

        logger.error(String.format("Не найден ключ конфигурации: %s", property));

        return null;
    }
}
