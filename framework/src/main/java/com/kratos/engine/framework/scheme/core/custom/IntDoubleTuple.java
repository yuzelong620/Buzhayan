package com.kratos.engine.framework.scheme.core.custom;

public class IntDoubleTuple {

    private int key;
    private double value;

    public IntDoubleTuple() {
    }

    public IntDoubleTuple(int key, double value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
