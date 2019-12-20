package com.kratos.engine.framework.event;

import com.google.common.eventbus.EventBus;

public class OnFire {
    private final static EventBus eventBus = new EventBus("global");

    public static void registerListener(Object listener) {
        eventBus.register(listener);
    }

    public static void fire(Object event) {
        eventBus.post(event);
    }
}
