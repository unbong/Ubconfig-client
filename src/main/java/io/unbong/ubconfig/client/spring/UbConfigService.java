package io.unbong.ubconfig.client.spring;

/**
 * Description
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-05-05 10:48
 */
public interface UbConfigService {


     public String[] getPropertyNames();


     public String getProperty(String name);
}
