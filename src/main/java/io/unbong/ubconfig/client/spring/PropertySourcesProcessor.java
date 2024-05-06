package io.unbong.ubconfig.client.spring;

import io.unbong.ubconfig.client.repository.UbRepository;
import io.unbong.ubconfig.client.spring.UbConfigService;
import io.unbong.ubconfig.client.spring.UbConfigServiceImpl;
import io.unbong.ubconfig.client.spring.UbPropertiesSource;
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

import java.util.HashMap;
import java.util.Map;

/**
 * ub property sources processor
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-05-05 10:56
 */
public class PropertySourcesProcessor implements BeanFactoryPostProcessor , ApplicationContextAware, PriorityOrdered, EnvironmentAware {


    private final static String UB_PROPERTY_SOURCES = "UbPropertySources";
    private final static String UB_PROPERTY_SOURCE = "UbPropertySource";
    private static final String UB_CONFIG_APP = "app1";
    private static final String UB_CONFIG_ENV = "dev";
    private static final String UB_CONFIG_NS = "public";
    private static final String UB_CONFIG_SERVER_URL = "http://localhost:9129";

    private Environment environment;

    private ApplicationContext applicationContext;

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        ConfigurableEnvironment configEnv = (ConfigurableEnvironment)environment;
        if( configEnv.getPropertySources().contains(UB_PROPERTY_SOURCES) ){
            return ;
        }

        // todo
        // get configuration from server by  http request
//        Map<String, String> config = new HashMap<String, String>();
//        config.put("ub.a", "dev199");
//        config.put("ub.b", "b199");
//        config.put("ub.c", "c99");

        String app =environment.getProperty(UB_CONFIG_APP, "app1");
        String env =environment.getProperty(UB_CONFIG_ENV, "dev");
        String ns =environment.getProperty(UB_CONFIG_NS, "public");
        String configUrl =environment.getProperty(UB_CONFIG_SERVER_URL, "http://localhost:9129");

        ConfigMeata configMeata = new ConfigMeata(app, env, ns, configUrl);


        UbConfigService configService = UbConfigService.getDefault(configMeata, applicationContext);

        UbPropertiesSource propertiesSource = new UbPropertiesSource(UB_PROPERTY_SOURCE, configService);

        CompositePropertySource composite = new CompositePropertySource(UB_PROPERTY_SOURCES);
        composite.addPropertySource(propertiesSource);
        configEnv.getPropertySources().addFirst(composite);

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
