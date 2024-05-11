package io.unbong.ubconfig.client.repository;

import io.unbong.ubconfig.client.spring.ConfigMeta;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * interface to get
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-05-06 10:26
 */
public interface UbRepository {

    static UbRepository getDefault(ConfigMeta meta, ApplicationContext applicationContext){
        return new UbRepositoryImpl(meta, applicationContext);
    };
    Map<String, String> getConfig();

    void addChangeListener(UbRepositoryChangeListener<Map<String,String>> listener);


}
