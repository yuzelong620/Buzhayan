package com.kratos.engine.framework.net.socket.annotation;

import java.lang.annotation.*;

/**
 * A method whose type is meta-annotated with this
 * is used to be a logic handler
 * @author herton
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageHandler {

}
