package io.unbong.ubconfig.client.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-05-05 10:52
 */
@Slf4j
public class UbConfigServiceImpl implements UbConfigService{

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

        Set<String> keys = calcChangeKeys(this.config, event.config());

        log.debug("------> [Config] changed config keys{}" ,keys);
        if(keys.isEmpty())
            return;
        this.config = event.config();
        applicationContext.publishEvent(new EnvironmentChangeEvent(keys));
    }

    private Set<String> calcChangeKeys(Map<String, String> oldConfs, Map<String, String> newConfs) {

        if(oldConfs.isEmpty()) return newConfs.keySet();

        if(newConfs.isEmpty()) return oldConfs.keySet();

        Set<String> news = newConfs.keySet().stream()
                .filter(key-> !newConfs.get(key).equals(oldConfs.get(key)))
                .collect(Collectors.toSet());

        oldConfs.keySet().stream().filter(key-> !newConfs.containsKey(key))
            .forEach(news::add);

        return news;
    }
}
