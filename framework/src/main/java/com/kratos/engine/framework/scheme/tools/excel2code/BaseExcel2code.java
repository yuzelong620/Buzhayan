package com.kratos.engine.framework.scheme.tools.excel2code;

import com.kratos.engine.framework.common.utils.StringHelper;
import com.kratos.engine.framework.scheme.core.utils.*;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * 解析excel生成对应代码
 */
@Log4j
public abstract class BaseExcel2code {
    private static String BASE_PATH = System.getProperty("user.dir");
    private final static String EXCEL_PATH = "/game/%s/src/main/resources/design/exls/";
    private final static String JAVA_BEAN_PATH = "/game/%s/src/main/java/com/kratos/game/%s/auto/server/%s";
    private final static String CLIENT_JS_PATH = "/game/%s/src/main/java/com/kratos/game/%s/auto/client/%s";
    private final static String SERVER_JSON_PATH = "/game/%s/src/main/resources/config/json/%s";
    private final static String STORAGE_NAME = "ConfigDataStorage";
    private final static String ENCODING = "utf-8";

    protected abstract String game();

    public void readExcel() throws IOException {
        reloadBasePath();
        String schemaPath = getExcelPath();
        File dir = new File(schemaPath);
        StringBuilder jsJsonData = new StringBuilder("const " + STORAGE_NAME + " = {};\n");
        ExcelData excelData;
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (!isExcel(file)) {
                continue;
            }

            if (file.getName().equalsIgnoreCase("GameParams.xlsx")) {
                excelData= GameParamExcelData.readExcel(file);
            } else {
                excelData = SimpleExcelData.readExcel(file);
            }
            log.info("读取文件---------->" + file.getPath());
            String jsonCode = excelData.getJsonCode();
            jsJsonData.append(STORAGE_NAME + ".").append(excelData.getFileName()).append(" = ").append(jsonCode)
                    .append(";").append("\n");
            createServerJavaFile(excelData);
            // 服务器文件
            FileUtils.writeStringToFile(new File(getServerJsonPath(excelData.getFileName())), jsonCode, ENCODING);
        }
        // 客户端文件
        FileUtils.writeStringToFile(new File(getClientJsPath()), jsJsonData.toString(), ENCODING);
    }

    private void createServerJavaFile(ExcelData excelData) {
        String javaFileName = excelData.getFileName() + "Config";
        TemplateFactory
                .builder()
                .setFile(new File(getJavaBeanPath(javaFileName)))
                .setTemplateName(Template.EXCEL_2_JAVA)
                .addParam("game", game())
                .addParam("className", javaFileName)
                .addParam("fields", Template.getFieldsBy(excelData.getFields()))
                .build()
                .process();
    }

    private boolean isExcel(File file) {
        return !file.isDirectory() && (file.getPath().endsWith(".xls") || file.getPath().endsWith(".xlsx"));
    }

    private void reloadBasePath() {
        BASE_PATH = System.getProperty("user.dir");
    }

    private String fullPath(String path) {
        return BASE_PATH + path;
    }

    private String getExcelPath() {
        return fullPath(String.format(EXCEL_PATH, game()));
    }

    private String getJavaBeanPath(String beanName) {
        return fullPath(String.format(JAVA_BEAN_PATH, game(), game(), StringHelper.toUpperCaseFirstOne(beanName) + ".java"));
    }

    private String getClientJsPath() {
        return fullPath(String.format(CLIENT_JS_PATH, game(), game(), "ConfigDataStorage.js"));
    }

    private String getServerJsonPath(String beanName) {
        return fullPath(String.format(SERVER_JSON_PATH, game(), StringHelper.toUpperCaseFirstOne(beanName) + ".json"));
    }
}
