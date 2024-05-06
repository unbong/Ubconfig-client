package io.unbong.ubconfig.client.spring;


import org.springframework.core.env.EnumerablePropertySource;

/**
 * Description
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-05-05 10:39
 */
public class UbPropertiesSource extends EnumerablePropertySource<UbConfigService> {

    public UbPropertiesSource(String name, UbConfigService source) {
        super(name, source);
    }

    /**
     *
     *
     * @return property names
     */
    public String[] getPropertyNames() {
        return source.getPropertyNames();
    }

    /**
     *
     * @param name
     * @return
     */
    public Object getProperty(String name) {
        return source.getProperty(name);
    }
}
