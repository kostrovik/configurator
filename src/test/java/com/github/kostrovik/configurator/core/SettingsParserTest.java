package com.github.kostrovik.configurator.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * project: configurator
 * author:  kostrovik
 * date:    05/07/2018
 * github:  https://github.com/kostrovik/configurator
 */
public class SettingsParserTest {
    private Properties properties;

    @BeforeEach
    void initProperties() {
        properties = new Properties();
        properties.setProperty("prop_single", "single_value");
        properties.setProperty("property.module.view.builder_main", "com.github.kostrovik.configurator.core.views.MainBuilder");
        properties.setProperty("property.module.menu.builder", "com.github.kostrovik.configurator.core.views.menu.MainBuilder");
    }

    @Test
    void countSettingsTest() {
        SettingsParser parser = new SettingsParser(properties);
        Map config = parser.getConfig();

        assertEquals(2, config.keySet().size());
    }

    @Test
    void configValueTest() {
        SettingsParser parser = new SettingsParser(properties);
        Map config = parser.getConfig();

        assertEquals("single_value", config.get("prop_single"));
        assertEquals("com.github.kostrovik.configurator.core.views.MainBuilder", ((Map) ((Map) ((Map) config.get("property")).get("module")).get("view")).get("builder_main"));
        assertEquals("com.github.kostrovik.configurator.core.views.menu.MainBuilder", ((Map) ((Map) ((Map) config.get("property")).get("module")).get("menu")).get("builder"));
    }

    @Test
    void configGetPropertyTest() {
        SettingsParser parser = new SettingsParser(properties);

        assertEquals("single_value", parser.getConfigProperty("prop_single"));
        assertEquals("com.github.kostrovik.configurator.core.views.MainBuilder", parser.getConfigProperty("builder_main"));
    }

    @Test
    void configGetPropertyFirstTest() {
        SettingsParser parser = new SettingsParser(properties);
        properties.setProperty("property.module.prop_single", "deep_property");

        assertNotEquals("deep_property", parser.getConfigProperty("prop_single"));
    }

    @Test
    void configGetPropertyNullTest() {
        SettingsParser parser = new SettingsParser(properties);

        assertNull(parser.getConfigProperty("undefined property"));
    }
}
