package com.kratos.engine.framework.scheme.support;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Log4j
public abstract class BaseConfigResourceRegistry {
    private ConfigDataStorage configDataStorage = new ConfigDataStorage();

    @PostConstruct
    private void init() {
        loadAllConfigs();
    }

    @SuppressWarnings("unchecked")
    private void loadAllConfigs() {
        Config config = config();
        List<ConfigData> configDataList = config.getConfigDataList();
        for (ConfigData configData : configDataList) {
            configData.reload();
            configDataStorage.put(configData.getEntityClass(), configData);
        }
    }

    protected abstract Config config();

    @Getter
    public static class Config {
        private List<ConfigData> configDataList;

        public Config(List<ConfigData> configDataList) {
            this.configDataList = configDataList;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private List<ConfigData> configDataList = new ArrayList<>();

            public Config.Builder addJson(ConfigData configData) {
                this.configDataList.add(configData);
                return this;
            }

            public Config build() {
                Assert.notEmpty(configDataList, "configDataList is needed");
                return new Config(configDataList);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> ConfigData<T> getConfig(Class<? extends T> clazz) {
        return configDataStorage.get(clazz);
    }
}
