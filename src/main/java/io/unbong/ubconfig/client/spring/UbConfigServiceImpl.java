package io.unbong.ubconfig.client.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Description
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-05-05 10:52
 */
public class UbConfigServiceImpl implements UbConfigService{

    Log log = LogFactory.getLog(this.getClass());
    Map<String, String> config;
    ApplicationContext applicationContext ;

    public UbConfigServiceImpl(Map<String, String> config, ApplicationContext applicationContext) {
        this.config = config;
        this.applicationContext = applicationContext;
    }

    public String[] getPropertyNames() {
        return this.config.keySet().toArray(new String[0]);
    }

    public String getProperty(String name) {
        return this.config.get(name);
    }

    @Override
    public void onChange(UbRepositoryChangeEvent<Map<String, String>> event) {
        log.debug("config changed. new config" + event.config());
        this.config = event.config();
        if(config.isEmpty())
            return;
        applicationContext.publishEvent(new EnvironmentChangeEvent(event.config().keySet()));
    }
}
