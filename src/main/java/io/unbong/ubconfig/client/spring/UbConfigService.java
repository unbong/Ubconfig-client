package io.unbong.ubconfig.client.spring;

import io.unbong.ubconfig.client.repository.UbRepository;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Description
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-05-05 10:48
 */
public interface UbConfigService {

     static UbConfigService getDefault(ConfigMeta meta, ApplicationContext applicationContext){
          Map<String, String> config = UbRepository.getDefault(meta, applicationContext).getConfig();

          return new UbConfigServiceImpl(config, applicationContext);
     }

     public String[] getPropertyNames();


     public String getProperty(String name);
}
