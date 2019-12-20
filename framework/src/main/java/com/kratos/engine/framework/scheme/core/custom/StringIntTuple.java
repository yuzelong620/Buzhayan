package com.kratos.engine.framework.scheme.core.custom;


public class StringIntTuple {

    private String key;
    private int value;
    private int subkey;

    public StringIntTuple() {
    }

    public StringIntTuple(Object key, int value) {
        this.key = key.toString();
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key.toString();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getSubkey() {
        return subkey;
    }

    public void setSubkey(int subkey) {
        this.subkey = subkey;
    }

    @Override
    public String toString() {
        return "<" + key + "," + value + ">";
    }

    public String tochyString() {
        return key + "_" + value;
    }
}
