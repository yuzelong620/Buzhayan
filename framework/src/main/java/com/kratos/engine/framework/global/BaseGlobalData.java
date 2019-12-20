package com.kratos.engine.framework.global;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

@Log4j
@Order
public abstract class BaseGlobalData {
    @Autowired
    private GlobalDataService globalDataService;

    @PostConstruct
    public void load() {
        List<GlobalDataProperty> globalDataProperties = this.globalDataService.findAll();
        // 给所有field填值
        for (GlobalDataProperty data : globalDataProperties) {
            String key = data.getId();
            String value = data.getValue();

            callSetter(key, value);
        }
    }

    private void callSetter(String filedName, String value) {
        Class selfClass = this.getClass();
        Field field;
        try {
            field = selfClass.getDeclaredField(filedName);
            if (!Modifier.isVolatile(field.getModifiers())) {
                throw new RuntimeException("SystemParameters 属性" + field.getName() + "缺少修饰符volatile");
            }
            Class fieldType = field.getType();
            if (fieldType == String.class) {
                field.set(this, value);
            } else if (fieldType == Byte.class || fieldType == byte.class) {
                field.set(this, Byte.valueOf(value));
            } else if (fieldType == Short.class || fieldType == short.class) {
                field.set(this, Short.parseShort(value));
            } else if (fieldType == Integer.class || fieldType == int.class) {
                field.set(this, Integer.parseInt(value));
            } else if (fieldType == Long.class || fieldType == long.class) {
                field.set(this, Long.parseLong(value));
            } else if (fieldType == Float.class || fieldType == float.class) {
                field.set(this, Float.valueOf(value));
            } else if (fieldType == Double.class || fieldType == double.class) {
                field.set(this, Double.parseDouble(value));
            } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                field.set(this, value.equalsIgnoreCase("1"));
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private void setFieldValue(String key, Object value) {
        try {
            Field field = this.getClass().getDeclaredField(key);
            if (field != null) {
                field.set(this, value);
            }
        } catch (Exception e) {
            log.error("dbutil setValue error", e);
        }
    }

    private void saveToDb(String key, String value) {
        // 入库
        GlobalDataProperty globalDataProperty = globalDataService.get(key);
        if(globalDataProperty == null) {
            globalDataProperty = new GlobalDataProperty();
            globalDataProperty.setId(key);
        }
        globalDataProperty.setValue(value);
        globalDataService.cacheAndPersist(key, globalDataProperty);
    }

    public synchronized void update(String key, byte value) {
        setFieldValue(key, value);
        saveToDb(key, Byte.toString(value));
    }

    public synchronized void update(String key, boolean value) {
        setFieldValue(key, value);
        saveToDb(key, value ? "1" : "0");
    }

    public synchronized void update(String key, short value) {
        setFieldValue(key, value);
        saveToDb(key, Short.toString(value));
    }

    public synchronized void update(String key, int value) {
        setFieldValue(key, value);
        saveToDb(key, Integer.toString(value));
    }

    public synchronized void update(String key, long value) {
        setFieldValue(key, value);
        saveToDb(key, Long.toString(value));
    }

    public synchronized void update(String key, float value) {
        setFieldValue(key, value);
        saveToDb(key, Float.toString(value));
    }

    public synchronized void update(String key, double value) {
        setFieldValue(key, value);
        saveToDb(key, Double.toString(value));
    }

    public synchronized void update(String key, String value) {
        setFieldValue(key, value);
        saveToDb(key, value);
    }

}
