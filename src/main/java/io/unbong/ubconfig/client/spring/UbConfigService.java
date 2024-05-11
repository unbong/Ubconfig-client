package io.unbong.ubconfig.client.spring;

import io.unbong.ubconfig.client.repository.UbRepository;
import io.unbong.ubconfig.client.repository.UbRepositoryChangeListener;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Description
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-05-05 10:48
 */
public interface UbConfigService extends UbRepositoryChangeListener<Map<String,String>> {

     static UbConfigService getDefault(ConfigMeta meta, ApplicationContext applicationContext){
          UbRepository repository = UbRepository.getDefault(meta, applicationContext);
          Map<String, String> config = repository.getConfig();
          UbConfigService configService =   new UbConfigServiceImpl(config, applicationContext);
          repository.addChangeListener(configService);
          return configService;
     }

     public String[] getPropertyNames();


     public String getProperty(String name);
}
