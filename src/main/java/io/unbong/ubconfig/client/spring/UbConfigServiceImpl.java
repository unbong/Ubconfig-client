package io.unbong.ubconfig.client.spring;

import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Description
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-05-05 10:52
 */
public class UbConfigServiceImpl implements UbConfigService{

    Map<String, String> config;

    public UbConfigServiceImpl(Map<String, String> config, ApplicationContext applicationContext) {
        this.config = config;
    }

    public String[] getPropertyNames() {
        return this.config.keySet().toArray(new String[0]);
    }

    public String getProperty(String name) {
        return this.config.get(name);
    }
}
