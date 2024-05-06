package io.unbong.ubconfig.client.repository;

import io.unbong.ubconfig.client.spring.ConfigMeata;

import java.util.Map;

/**
 * interface to get
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-05-06 10:26
 */
public interface UbRepository {

    static UbRepository getDefault(ConfigMeata meata){
        return new UbRepositoryImpl(meata);
    };
    Map<String, String> getConfig();

    interface  ChangeListener{
        void onChange(ChangeEvent event);
    }

    record  ChangeEvent(ConfigMeata meta, Map<String, String> config){};

}
