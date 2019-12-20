package com.kratos.engine.framework.scheme.core.utils;

import org.springframework.util.Assert;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TemplateFactory {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Map<String, Object> params = new HashMap<>();
        private String templateName;
        private File file;

        public Builder setTemplateName(String templateName) {
            this.templateName = templateName;
            return this;
        }

        public Builder setFile(File file) {
            this.file = file;
            return this;
        }

        public Builder addParam(String key, Object param) {
            this.params.put(key, param);
            return this;
        }

        public Template build() {
            Assert.hasText(templateName, "templateName is needed");
            Assert.notNull(file, "file is needed");
            return new Template(templateName, params, file);
        }
    }
}
