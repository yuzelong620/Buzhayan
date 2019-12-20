package com.kratos.game.herphone.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当该批注修饰一个方法时，在Controller切面中会对请求该方法的用户进行鉴权。 class 上面的权限 + "_" + method 上面的权限组合
 *
 * @author herton
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PrePermissions {

    /**
     * 是否需要鉴权
     */
    boolean required() default true;
}
