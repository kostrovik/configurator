package com.github.kostrovik.configurator.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * project: configurator
 * author:  kostrovik
 * date:    08/07/2018
 * github:  https://github.com/kostrovik/configurator
 */
public class ModuleConfiguratorTest {
    private String configFile = "test_config.properties";
    private URL configUrl;

    @BeforeEach
    void initProperties() {
        configUrl = this.getClass().getClassLoader().getResource(configFile);
    }

    @Test
    void cyrillicPropertyValueTest() {
        assertNotNull(configUrl);

        ModuleConfigurator configurator = new ModuleConfigurator(configUrl.getPath());
        Map<String, Object> config = configurator.getConfig();

        assertEquals("Кирилическое название", config.get("prop"));
    }
}
