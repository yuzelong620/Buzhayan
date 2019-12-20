package com.kratos.engine.framework.scheme.core.custom;


/**
 * <string,float>
 *
 * @author ninglong
 */
public class StringFloatTuple {

    private String key;
    private float value;

    public StringFloatTuple() {
    }

    public StringFloatTuple(Object key, float value) {
        this.key = key.toString();
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key.toString();
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "<" + key + "," + value + ">";
    }
}