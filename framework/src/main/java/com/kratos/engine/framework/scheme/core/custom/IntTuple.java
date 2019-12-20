package com.kratos.engine.framework.scheme.core.custom;

public class IntTuple {

    private int key;
    private int value;

    public IntTuple() {
    }

    public IntTuple(int key, int value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    @Deprecated
    public void setKey(int key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    @Deprecated
    public void setValue(int value) {
        this.value = value;
    }
}
