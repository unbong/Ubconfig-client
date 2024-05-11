package io.unbong.ubconfig.client.value;

import cn.kimmking.utils.FieldUtils;
import io.unbong.ubconfig.client.utils.PlaceholderHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * process spring value annotation
 *  1 scan all spring value to save
 *  2 update spring value when config changed
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-05-11 15:26
 */
@Slf4j
public class SpringValueProcessor implements BeanPostProcessor, BeanFactoryAware,ApplicationListener<EnvironmentChangeEvent> {


    static final PlaceholderHelper helper = PlaceholderHelper.getInstance();
    static final MultiValueMap<String, SpringValue> VALUE_HOLDER = new LinkedMultiValueMap<>() ;
    private BeanFactory beanFactory;

    /**
     *  called when all bean initialization
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        // scan bean to get @Value annotation object and expression
        FieldUtils.findAnnotatedField(bean.getClass(), Value.class).forEach(
                field -> {
                    log.info("----> [config]find spring value: {}" ,field);
                    Value value = field.getAnnotation(Value.class);
                    value.value();
                    helper.extractPlaceholderKeys(value.value()).forEach(
                            key->{
                                SpringValue springValue = new SpringValue(bean, beanName, key, value.value(), field);

                                VALUE_HOLDER.add(key, springValue);
                            }
                    );


                }
        );
        return bean;
    }

//    @EventListener
//    public void onChange(EnvironmentChangeEvent event)
//    {
//
//    }

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        event.getKeys().forEach(key->{

            log.info("----> [config] update spring value: {}" ,key);

            List<SpringValue> springValues = VALUE_HOLDER.get(key);

            if(springValues == null || springValues.isEmpty())
                return;
            springValues.forEach(springValue -> {

                log.info("----> [config] update spring value: {}" ,springValue);

                try{
                    Object value = helper.resolvePropertyValue( (ConfigurableBeanFactory)beanFactory, springValue.getBeanName(),
                            springValue.getPlaceHolder());
                    springValue.getField().setAccessible(true);
                    springValue.getField().set(springValue.getBean(), value);
                }catch (Exception ex)
                {
                    log.error(ex.getMessage());
                }

            });
        });
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
