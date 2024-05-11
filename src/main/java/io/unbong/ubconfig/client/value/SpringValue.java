package io.unbong.ubconfig.client.value;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * Description
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-05-11 15:57
 */
@Data
@AllArgsConstructor
public class SpringValue {

    private Object bean;
    private String beanName;
    private String key;
    private String placeHolder;     //
    private Field field;            //
}
