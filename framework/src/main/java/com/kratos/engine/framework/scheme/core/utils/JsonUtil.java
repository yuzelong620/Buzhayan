package com.kratos.engine.framework.scheme.core.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kratos.engine.framework.common.utils.StringHelper;
import com.kratos.engine.framework.scheme.core.custom.*;
import lombok.extern.log4j.Log4j;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class JsonUtil {

    @SuppressWarnings("unchecked")
    public static <T> List<T> parseJsonArray(File jsonFile, Class<T> class1) {
        try {
            List result;
            JSONArray jsonArray = readConfigFile(jsonFile);
            result = new ArrayList<T>();
            Field[] fields = class1.getDeclaredFields();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                T t = class1.newInstance();
                for (Field field : fields) {
                    String fieldName = field.getName();
                    if (obj.containsKey(fieldName) && obj.get(fieldName) != null) {
                        Method method = class1.getDeclaredMethod("set" + StringHelper.toUpperCaseFirstOne(fieldName), field.getType());
                        if (field.getType() == String.class) {
                            method.invoke(t, obj.getString(fieldName));
                        } else if (field.getType() == Integer.TYPE || field.getType() == Integer.class) {
                            method.invoke(t, obj.getIntValue(fieldName));
                        } else if (field.getType() == Short.TYPE || field.getType() == Short.class) {
                            method.invoke(t, obj.getIntValue(fieldName));
                        } else if (field.getType() == Byte.TYPE || field.getType() == Byte.class) {
                            method.invoke(t, obj.getIntValue(fieldName));
                        } else if (field.getType() == Long.TYPE || field.getType() == Long.class) {
                            method.invoke(t, obj.getLongValue(fieldName));
                        } else if (field.getType() == Float.TYPE || field.getType() == Float.class) {
                            method.invoke(t, obj.getDoubleValue(fieldName));
                        } else if (field.getType() == Double.TYPE || field.getType() == Double.class) {
                            method.invoke(t, obj.getDoubleValue(fieldName));
                        } else if (field.getType() == Boolean.TYPE || field.getType() == Boolean.class) {
                            method.invoke(t, obj.getBooleanValue(fieldName));
                        } else if (field.getType() == IntTuple.class) {
                            IntTuple intTuple = parseIntIntTuple(obj.getJSONObject(fieldName));
                            method.invoke(t, intTuple);
                        } else if (field.getType() == StringIntTuple.class) {
                            StringIntTuple intTuple = parseStringIntTuple(obj.getJSONObject(fieldName));
                            method.invoke(t, intTuple);
                        } else if (field.getType() == ThreeTuple.class) {
                            ThreeTuple threeTuple = parseThreeTuple(obj.getJSONObject(fieldName));
                            method.invoke(t, threeTuple);
                        } else if (field.getType() == StringFloatTuple.class) {
                            StringFloatTuple stringFloatTuple = parseStringFloatTuple(obj.getJSONObject(fieldName));
                            method.invoke(t, stringFloatTuple);
                        } else if (field.getType() == List.class) {
                            JSONArray array = obj.getJSONArray(fieldName);
                            List list = null;
                            if(array != null) {
                                switch (field.getGenericType().toString()) {
                                    case "java.util.List<java.lang.String>":
                                        list = new ArrayList<String>(array.size());
                                        for (int m = 0; m < array.size(); m++) {
                                            list.add(array.getString(m));
                                        }
                                        break;
                                    case "java.util.List<com.kratos.engine.framework.scheme.core.custom.ThreeTuple>":
                                        list = new ArrayList<ThreeTuple>(array.size());
                                        for (int m = 0; m < array.size(); m++) {
                                            list.add(parseThreeTuple(array.getJSONObject(m)));
                                        }
                                        break;
                                    case "java.util.List<com.kratos.engine.framework.scheme.core.custom.StringFloatTuple>":
                                        list = new ArrayList<StringFloatTuple>(array.size());
                                        for (int m = 0; m < array.size(); m++) {
                                            list.add(parseStringFloatTuple(array.getJSONObject(m)));
                                        }
                                        break;
                                    case "java.util.List<com.kratos.engine.framework.scheme.core.custom.StringIntTuple>":
                                        list = new ArrayList<StringIntTuple>(array.size());
                                        for (int m = 0; m < array.size(); m++) {
                                            list.add(parseStringIntTuple(array.getJSONObject(m)));
                                        }
                                        break;
                                    case "java.util.List<com.kratos.engine.framework.scheme.core.custom.IntDoubleTuple>":
                                        list = new ArrayList<IntDoubleTuple>(array.size());
                                        for (int m = 0; m < array.size(); m++) {
                                            list.add(parseIntDoubleTuple(array.getJSONObject(m)));
                                        }
                                        break;
                                    case "java.util.List<com.kratos.engine.framework.scheme.core.custom.IntTuple>":
                                        list = new ArrayList<IntTuple>(array.size());
                                        for (int m = 0; m < array.size(); m++) {
                                            list.add(parseIntIntTuple(array.getJSONObject(m)));
                                        }
                                        break;
                                    case "java.util.List<java.lang.Integer>":
                                        list = new ArrayList<Integer>(array.size());
                                        for (int m = 0; m < array.size(); m++) {
                                            list.add(array.getIntValue(m));
                                        }
                                        break;
                                    case "java.util.List<java.lang.Double>":
                                        list = new ArrayList<Double>(array.size());
                                        for (int m = 0; m < array.size(); m++) {
                                            list.add(array.getDoubleValue(m));
                                        }
                                        break;
                                }
                            }
                            method.invoke(t, list);
                        }
                    }
                }
                result.add(t);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static StringIntTuple parseStringIntTuple(JSONObject jsonObject) {
        return new StringIntTuple(jsonObject.getString("key"), jsonObject.getIntValue("value"));
    }

    private static StringFloatTuple parseStringFloatTuple(JSONObject jsonObject) {
        return new StringFloatTuple(jsonObject.getString("key"), jsonObject.getFloatValue("value"));
    }

    private static ThreeTuple parseThreeTuple(JSONObject jsonObject) {
        return new ThreeTuple(jsonObject.getString("key"), jsonObject.getIntValue("value"), jsonObject.getIntValue("probability"));
    }

    private static IntDoubleTuple parseIntDoubleTuple(JSONObject jsonObject) {
        return new IntDoubleTuple(jsonObject.getIntValue("key"), jsonObject.getDoubleValue("value"));
    }

    private static IntTuple parseIntIntTuple(JSONObject jsonObject) {
        return new IntTuple(jsonObject.getIntValue("key"), jsonObject.getIntValue("value"));
    }

    private static JSONArray readConfigFile(File file) throws IOException {
        JSONArray result = null;
        Reader reader;
        StringBuffer sb = new StringBuffer();
        if (file.exists()) {
            FileInputStream fileStream = new FileInputStream(file);//读取外部目录文件
            reader = new InputStreamReader(fileStream, "utf-8");
            BufferedReader fb = new BufferedReader(reader);

            String s;
            while ((s = fb.readLine()) != null) {
                sb = sb.append(s);
            }
        }
        if(StringHelper.isNoneBlank(sb)) {
            result = JSONArray.parseArray(sb.toString());
        } else {
            log.error("failed to read file " + file.getName());
        }
        return result;
    }
}
