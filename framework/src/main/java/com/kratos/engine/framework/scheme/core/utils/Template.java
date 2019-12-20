package com.kratos.engine.framework.scheme.core.utils;

import freemarker.template.*;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;

@Log4j
public class Template {
    private final static String FTL_DIR = "/scheme/ftl";
    private final static String CHARSET_NAME = "utf-8";

    public final static String EXCEL_2_JAVA = "excel2java.ftl";
    public final static String EXCEL_2_JS = "excel2js.ftl";

    private Configuration cfg;
    private Map<String, Object> params = new HashMap<>();
    private String templateName;
    private File file;

    public Template(String templateName, Map<String, Object> params, File file) {
        // 初始化FreeMarker配置;
        // - 创建一个配置实例
        cfg = new Configuration();
        // - 设置模板目录.
        cfg.setClassForTemplateLoading(this.getClass(), FTL_DIR);
        // - 设置模板延迟时间，测试环境设置为0，正是环境可提高数值.
        cfg.setTemplateUpdateDelay(0);
        // - 设置错误句柄
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
        cfg.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
        // - 设置默认模板编码
        cfg.setDefaultEncoding(CHARSET_NAME);
        // - 设置输出编码
        cfg.setOutputEncoding(CHARSET_NAME);
        cfg.setLocale(Locale.SIMPLIFIED_CHINESE);
        this.templateName = templateName;
        this.params = params;
        this.file = file;
    }

    public void process() {
        try {
            FileUtils.forceMkdirParent(file);
        } catch (IOException e) {
            log.error("", e);
        }
        try (Writer out = new OutputStreamWriter(new FileOutputStream(file), CHARSET_NAME)) {

            freemarker.template.Template t = this.cfg.getTemplate(templateName);
            t.process(params, out);
        } catch (IOException | TemplateException e) {
            log.error("", e);
        }
    }

    public static List<Object> getFieldsBy(List<ExcelColumn> columns) {
        List<Object> list = new ArrayList<>();
        for (ExcelColumn fieldModel : columns) {
            Map<String, Object> field = new HashMap<>();
            field.put("fieldDesc", fieldModel.getColumnName());
            field.put("fieldType", fieldModel.getJavaType());
            field.put("fieldName", fieldModel.getFieldName());
            field.put("fieldIsText", 0);
            field.put("fieldIsList", 0);
            list.add(field);
        }
        return list;
    }
}
