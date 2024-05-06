package io.unbong.ubconfig.client.repository;

import cn.kimmking.utils.HttpUtils;
import com.alibaba.fastjson.TypeReference;
import io.unbong.ubconfig.client.spring.ConfigMeata;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * default impl for ub repository
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-05-06 10:28
 */


public class UbRepositoryImpl implements UbRepository{

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    Log log = LogFactory.getLog(this.getClass());
    public UbRepositoryImpl(ConfigMeata meata) {
        this.meata = meata;

        executorService.scheduleWithFixedDelay(this::hearthBeat, 1000, 5000, TimeUnit.MILLISECONDS);
    }

    ConfigMeata meata;

    Map<String, Long> versions = new HashMap<>();

    Map<String, Map<String, String>> configMap = new HashMap<>();

    private void hearthBeat(){
        String versionPath  = meata.versionPath();

        Long version = HttpUtils.httpGet(versionPath,new TypeReference<Long>(){});
        String key = meata.genKey();
        Long oldVersion = versions.getOrDefault(key, -1l);
        if(version > oldVersion){
            log.debug("------> [CONFIG]  need update. config version " +  version + " oldVersion " + oldVersion);
            versions.put(key, version);
            configMap.put(key, findAll());
        }
    }

    @Override
    public Map<String, String> getConfig() {

        String key = meata.genKey();
        if(configMap.containsKey(key))
        {
            return configMap.get(key);
        }

        return findAll();
    }

    @NotNull
    private Map<String, String> findAll() {
        String listPath = meata.getConfigUrl() + "/list?app=" + meata.getApp()
                +"&env="+meata.getEnv()
                +"&ns="+meata.getNs();

        List<Configs> configs =HttpUtils.httpGet(listPath, new TypeReference<List<Configs>>(){});

        Map<String, String> resultMap = new HashMap<>();
        configs.forEach(c-> resultMap.put(c.getPkey(), c.getPval()));
        return resultMap;
    }


}