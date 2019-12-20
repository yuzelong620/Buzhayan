package com.kratos.engine.framework.scheme.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j;

import java.io.File;
import java.util.List;

@Log4j
public class GameParamExcelData extends ExcelData {
    private GameParamExcelData(File file) {
        super(file);
    }


    public static GameParamExcelData readExcel(File file) {
        return new GameParamExcelData(file);
    }

    @Override
    List<ExcelColumn> readSchema(File file) {
        List<ExcelColumn> columns = Lists.newArrayList();
        ExcelColumn column;
        dataList = ExcelUtils.readExcel(file, null);
        for (int i = 1; i < dataList.size(); i++) {
            String des = dataList.get(i).get(0);
            String name = dataList.get(i).get(1);
            String type = dataList.get(i).get(2);
            String value = dataList.get(i).get(3);
            column = new ExcelColumn(des, name, type, value);
            columns.add(column);
        }
        return columns;
    }

    @Override
    public String getJsonCode() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj = new JSONObject();
        for (ExcelColumn column : fields) {
            jsonObj.put(column.getFieldName(), column.convert(column.getDefaultValue()));
        }
        jsonArray.add(jsonObj);
        return JSON.toJSONString(jsonArray, SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
    }
}
