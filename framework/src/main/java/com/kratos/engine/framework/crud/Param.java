package com.kratos.engine.framework.crud;

import lombok.Data;

@Data
public class Param {
    private String key;
    private Object value;
    private Condition condition;

    private Param() {}

    private Param(String key, Object value, Condition condition) {
        this.key = key;
        this.value = value;
        this.condition = condition;
    }

    public static Param equal(String key, Object value) {
        return new Param(key, value, Condition.equal);
    }

    public enum Condition {
        equal;
    }
}
