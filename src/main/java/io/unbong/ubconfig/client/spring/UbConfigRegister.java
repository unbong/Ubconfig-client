package io.unbong.ubconfig.client.spring;

import io.unbong.ubconfig.client.value.SpringValueProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.Optional;

/**
 * register ub config bean
 *≤
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-05-05 16:00≤
 */

@Slf4j
public class UbConfigRegister implements ImportBeanDefinitionRegistrar {
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        log.info("----> [config] regist bean" );

        registerClass(registry, PropertySourcesProcessor.class);
        registerClass(registry, SpringValueProcessor.class);


    }

    private static void registerClass(BeanDefinitionRegistry registry, Class<?> clazz) {
        Optional<String> first =Arrays.stream(registry.getBeanDefinitionNames()).filter(
                x-> clazz.getName().equals(x)).findFirst();

        if(first.isEmpty())
        {
            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.
                    genericBeanDefinition(clazz).getBeanDefinition();
            registry.registerBeanDefinition(clazz.getName(), beanDefinition);
        }
    }

}
