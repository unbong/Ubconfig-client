package io.unbong.ubconfig.client.annotation;


import io.unbong.ubconfig.client.spring.UbConfigRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


/**
 *
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Import({UbConfigRegister.class})
public @interface EnableUbConfig {

}
