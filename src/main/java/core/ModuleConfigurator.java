package core;

import interfaces.ModuleConfiguratorInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * project: configurator
 * author:  kostrovik
 * date:    05/07/2018
 * github:  https://github.com/kostrovik/configurator
 */
public class ModuleConfigurator implements ModuleConfiguratorInterface {
    private static Logger logger = LogManager.getLogger(ModuleConfigurator.class);
    private static Map<String, Object> config;
    private final static String defaultConfigFilePath = "configurations/module_config.properties";
    private SettingsParser parser;

    public ModuleConfigurator() {
        this.config = parseConfig(getDefaultConfig());
    }

    public ModuleConfigurator(Properties properties) {
        this.config = parseConfig(properties);
    }

    public ModuleConfigurator(String configPath) {
        this.config = parseConfig(configPath);
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    private Map<String, Object> parseConfig(Properties customSettings) {
        Properties result = getDefaultConfig();

        for (Object key : customSettings.keySet()) {
            if (result.containsKey(key)) {
                result.setProperty((String) key, customSettings.getProperty((String) key));
            }
        }

        parser = new SettingsParser(customSettings);

        return parser.getConfig();
    }

    private Map<String, Object> parseConfig(String configPath) {
        return parseConfig(getCustomConfig(configPath));
    }

    private Properties getDefaultConfig() {
        Properties result = new Properties();

        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(defaultConfigFilePath)) {
            if (inputStream != null) {
                result.load(inputStream);
            }
        } catch (FileNotFoundException error) {
            logger.error("Не найден файл конфигурации по умолчанию", error);
        } catch (IOException error) {
            logger.error("Не возможно загрузить настройки умолчанию", error);
        }

        return result;
    }

    private Properties getCustomConfig(String configPath) {
        Properties result = new Properties();

        try (FileInputStream inputStream = new FileInputStream(configPath)) {
            result.load(inputStream);
        } catch (FileNotFoundException error) {
            logger.error(String.format("Не найден файл конфигурации: %s", configPath), error);
        } catch (IOException error) {
            logger.error("Не возможно загрузить конфигурацию", error);
        }

        return result;
    }
}
