package com.github.kostrovik.configurator.interfaces;

import java.util.Map;

/**
 * project: configurator
 * author:  kostrovik
 * date:    05/07/2018
 * github:  https://github.com/kostrovik/configurator
 */
public interface ModuleConfiguratorInterface {
    Map<String, Object> getConfig();

    Map<String, Object> getModuleMenu();

    Map<String, String> getViews();
}
