package io.unbong.ubconfig.client.spring;

import cn.kimmking.utils.HttpUtils;
import com.alibaba.fastjson.TypeReference;
import io.unbong.ubconfig.client.meta.InstanceMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * ub property sources processor
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-05-05 10:56
 */
@Slf4j
public class PropertySourcesProcessor implements BeanFactoryPostProcessor , ApplicationContextAware, PriorityOrdered, EnvironmentAware {


    private final static String UB_PROPERTY_SOURCES = "UbPropertySources";
    private final static String UB_PROPERTY_SOURCE = "UbPropertySource";
    private static final String UB_CONFIG_APP = "app1";
    private static final String UB_CONFIG_ENV = "dev";
    private static final String UB_CONFIG_NS = "public";
    private static final String UB_CONFIG_SERVER_URL = "configServerUrl";
    private static final String UB_REGISTRY_URL = "ubregistry.servers";

    private Environment environment;

    private ApplicationContext applicationContext;

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        ConfigurableEnvironment configEnv = (ConfigurableEnvironment)environment;
        if( configEnv.getPropertySources().contains(UB_PROPERTY_SOURCES) ){
            return ;
        }

        String configServerUrl = getConfigServerUrl();

        String app =environment.getProperty(UB_CONFIG_APP, "app1");
        String env =environment.getProperty(UB_CONFIG_ENV, "dev");
        String ns =environment.getProperty(UB_CONFIG_NS, "public");
        String configUrl =environment.getProperty(UB_CONFIG_SERVER_URL, "http://localhost:9129");
        if(StringUtils.hasText(configServerUrl))
        {
            configUrl = configServerUrl;
        }


        ConfigMeta configMeta = new ConfigMeta(app, env, ns, configUrl);

        UbConfigService configService = UbConfigService.getDefault(configMeta, applicationContext);

        UbPropertiesSource propertiesSource = new UbPropertiesSource(UB_PROPERTY_SOURCE, configService);

        CompositePropertySource composite = new CompositePropertySource(UB_PROPERTY_SOURCES);
        composite.addPropertySource(propertiesSource);
        configEnv.getPropertySources().addFirst(composite);

    }

    private String getConfigServerUrl(){

        String registryUrl = environment.getProperty(UB_REGISTRY_URL,"http://localhost:8484") +"/findAll?service=app1_public_dev_ubconfigserver";
        log.debug("----> registryUrl: {}",registryUrl);
        List<InstanceMeta> configServerMeta = HttpUtils.httpGet(registryUrl, new TypeReference<List<InstanceMeta>>(){});
        log.debug("----> configServerMeta: {}", configServerMeta);

        if(configServerMeta == null || configServerMeta.isEmpty())
            return  null;
        return configServerMeta.get(0).toURL();
    }

    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
