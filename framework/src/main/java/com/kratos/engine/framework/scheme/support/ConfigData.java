package com.kratos.engine.framework.scheme.support;

import com.kratos.engine.framework.scheme.core.utils.JsonUtil;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j
@Data
public class ConfigData<E> {
    private List<E> list;
    private Map<Integer, E> map;
    private final Class<E> entityClass;
    private final String jsonName;
    protected boolean isObject;

    public ConfigData(Class<E> entityClass, String jsonName) {
        this.entityClass = entityClass;
        this.jsonName = jsonName;
        this.isObject = false;
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    @SuppressWarnings("unchecked")
    public void reload() {
        File sourceFile = null;
        File file = new File("json/" + jsonName);
        if (file.exists()) {
            sourceFile = file;
        } else {
            Resource resource = new ClassPathResource("config/json/" + jsonName);
            try {
                sourceFile = resource.getFile();
            } catch (IOException e) {
                log.error("", e);
            }
        }
        if (sourceFile == null) {
            throw new RuntimeException(String.format("config file [%s] not found", jsonName));
        }
        try {
            List list = JsonUtil.parseJsonArray(sourceFile, entityClass);
            setList(list);
        } catch (Exception e) {
            log.error(jsonName + "：数据装载错误");
            throw e;
        }

    }

    private void setList(List<E> datas) {
        if (datas == null) {
            return;
        }
        list = datas;
        Map<Integer, E> dataMap = new HashMap<>();
        for (E obj : datas) {
            try {
                if (isObject) {
                    continue;
                }
                int id = (Integer) obj.getClass().getMethod("getId").invoke(obj);
                dataMap.put(id, obj);
            } catch (Exception e) {
                throw new RuntimeException("not find: getId() mothed. class:  " + obj.getClass());
            }
        }
        this.map = dataMap;
        setDataCache(datas);
    }

    /**
     * 实现自己的缓存，请实现此方法
     */
    protected void setDataCache(List<E> datas) {

    }

    public List<E> getList() {
        return list;
    }

    public E getData(int id) {
        return map.get(id);
    }
}
