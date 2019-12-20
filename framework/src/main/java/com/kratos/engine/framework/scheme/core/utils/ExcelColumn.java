package com.kratos.engine.framework.scheme.core.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kratos.engine.framework.common.utils.StringHelper;
import com.kratos.engine.framework.scheme.core.custom.*;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.util.*;

@Data
@Log4j
public class ExcelColumn {
    /**
     * 用来拆分具有<..><..><..>形式的字符串正则表达式
     */
    public static final String GROUP_SPLITER = "(?<=>)(?=<)";
    /**
     * 用来拆分具有v1|v2|v3形式的字符串表达式
     */
    public static final String ELEMENT_SPLITER = ",";
    public static final char ELEMENT_SPLITER_CHAR = ',';

    private short columnIndex;//列索引，定义为short类型,因为HSSFRow#getCell方法需要传入short类型的参数
    private String columnName;//列名
    private String fieldName;//与该列对应的模板类型字段
    private String columnType;//类型
    private boolean isNumber;//是否数字类型（整形或者浮点型）
    private boolean isInteger;//是否是整数类型
    private boolean isFloat; //是否是浮点类型
    private Class<?> setterParamType; //setter的参数类型
    private Class<?> setterElementType; //当setter的参数类型有泛型时，如List<T>,setterElementName指出泛型参数的类型
    private String javaType;
    private boolean nullable; //是否可以为空
    private String defaultValue; //默认值

    //从HSSFRow构造ExcelColumn
    public ExcelColumn(XSSFRow row) {
        this.columnIndex = (short) (row.getRowNum() - 1);
        short cellIndex = 0;
        this.columnName = getCellString(row, cellIndex++);
        this.fieldName = getCellString(row, cellIndex++);
        this.setType(getCellString(row, cellIndex++));
        this.nullable = "yes".equals(getCellString(row, cellIndex++));
        this.defaultValue = getCellString(row, cellIndex);
    }

    public ExcelColumn(String columnName, String fieldName, String type, String value) {
        this.columnName = columnName;
        this.fieldName = fieldName;
        this.setType(type);
        this.defaultValue = value;
        this.nullable = false;
    }

    private void setType(String cellStr) {
        this.columnType = cellStr.toLowerCase();
        this.isInteger = this.columnType.contains("integer")
                || this.columnType.contains("long")
                || this.columnType.contains("int")
                || this.columnType.contains("short");
        this.isFloat = this.columnType.contains("float")
                || this.columnType.contains("double");
        this.isNumber = this.isInteger || this.isFloat;
        boolean isStringIntTuple = this.columnType.contains("stringint");
        boolean isListList = this.columnType.contains("list<list>");
        boolean isIntTuple = this.columnType.contains("intint");
        boolean isIntDoubleTuple = this.columnType.contains("intdouble") || this.columnType.contains("intfloat");
        boolean isList = this.columnType.contains("list");
        boolean isThreeTuple = this.columnType.contains("three");
        boolean isStringFloat = this.columnType.contains("stringfloat");
        if (isList) {
            if (isListList) {
                setterElementType = List.class;
                this.javaType = "List<List>";
            } else if (isStringFloat) {
                setterElementType = StringFloatTuple.class;
                this.javaType = "List<StringFloatTuple>";
            } else if (isThreeTuple) {
                setterElementType = ThreeTuple.class;
                this.javaType = "List<ThreeTuple>";
            } else if (isStringIntTuple) {
                setterElementType = StringIntTuple.class;
                this.javaType = "List<StringIntTuple>";
            } else if (isIntDoubleTuple) {
                setterElementType = IntDoubleTuple.class;
                this.javaType = "List<IntDoubleTuple>";
            } else if (isIntTuple) {
                setterElementType = IntTuple.class;
                this.javaType = "List<IntTuple>";
            } else if (isInteger) {
                setterElementType = Integer.class;
                this.javaType = "List<Integer>";
            } else if (isFloat) {
                setterElementType = Double.class;
                this.javaType = "List<Double>";
            } else {
                setterElementType = String.class;
                this.javaType = "List<String>";
            }
            setterParamType = List.class;
        } else if (isStringFloat) {
            setterParamType = StringFloatTuple.class;
            this.javaType = "StringFloatTuple";
        } else if (isThreeTuple) {
            setterParamType = ThreeTuple.class;
            this.javaType = "ThreeTuple";
        } else if (isStringIntTuple) {
            setterParamType = StringIntTuple.class;
            this.javaType = "StringIntTuple";
        } else if (isIntTuple) {
            setterParamType = IntTuple.class;
            this.javaType = "IntTuple";
        } else if (isInteger) {
            setterParamType = Integer.class;
            this.javaType = "Integer";
        } else if (isFloat) {
            setterParamType = Double.class;
            this.javaType = "Double";
        } else {
            setterParamType = String.class;
            this.javaType = "String";
        }
    }

    private static String getCellString(XSSFRow row, short cellIdx) {
        return ExcelUtils.getStringValue(row.getCell(cellIdx, Row.RETURN_NULL_AND_BLANK));
    }

    public Object convert(String cellStr) {
        if (this.setterParamType == String.class) {
            try {
                return StringUtils.replaceEachRepeatedly(cellStr, new String[]{"\\n"}, new String[]{"\n"});
            } catch (Exception e) {
                log.error("", e);
            }
        }
        if (this.setterParamType == Integer.TYPE || this.setterParamType == Integer.class) {
            if(StringHelper.isBlank(cellStr)) {
                return null;
            }
            return (int) Math.round(Double.parseDouble(cellStr));
        }
        if (this.setterParamType == Short.TYPE || this.setterParamType == Short.class) {
            return Short.parseShort(cellStr);
        }
        if (this.setterParamType == Byte.TYPE || this.setterParamType == Byte.class) {
            return Byte.parseByte(cellStr);
        }
        if (this.setterParamType == Long.TYPE || this.setterParamType == Long.class) {
            return Long.parseLong(cellStr);
        }
        if (this.setterParamType == Float.TYPE || this.setterParamType == Float.class) {
            return Float.parseFloat(cellStr);
        }
        if (this.setterParamType == Double.TYPE || this.setterParamType == Double.class) {
            if(StringHelper.isBlank(cellStr)) {
                return null;
            }
            return Double.parseDouble(cellStr);
        }
        if (this.setterParamType == Boolean.TYPE || this.setterParamType == Boolean.class) {
            return "1".equals(cellStr);
        }
        if (this.setterParamType == IntTuple.class) {
            return toIntTuple(cellStr);
        }
        if (this.setterParamType == StringIntTuple.class) {
            return toStringIntTuple(cellStr);
        }
        if (this.setterParamType == ThreeTuple.class) {
            return toThreeTuple(cellStr);
        }
        if (this.setterParamType == StringFloatTuple.class) {
            return toStringFloatTuple(cellStr);
        }
        if (this.setterParamType == List.class) {
            if(StringHelper.isBlank(cellStr)) {
                return null;
            }
            if (this.setterElementType == List.class) {
                return toListList(cellStr);
            }
            if (this.setterElementType == StringIntTuple.class) {
                return toStringIntTupleList(cellStr);
            } else if (this.setterElementType == IntDoubleTuple.class) {
                return toIntDoubleTupleList(cellStr);
            } else if (this.setterElementType == IntTuple.class) {
                return toIntTupleList(cellStr);
            } else if (this.setterElementType == Integer.class) {
                return toIntList(cellStr);
            } else if (this.setterElementType == Double.class) {
                return toDoubleList(cellStr);
            } else if (this.setterElementType == ThreeTuple.class) {
                return toThreeTupleList(cellStr);
            } else if (this.setterElementType == StringFloatTuple.class) {
                return toStringFloatTupleList(cellStr);
            } else {
                return toStringList(cellStr);
            }
        }
        if (this.setterParamType == Set.class) {
            if (this.setterElementType.equals("Integer")) {
                return toIntSet(cellStr);
            }
        }
        if (this.setterParamType.isArray()) {
            if (this.setterParamType.getComponentType() == int.class) {
                return toIntArray(cellStr);
            }
        }
        if (this.setterParamType == Map.class) {
            if (this.setterElementType.equals("Int|Int")) {
                return toIntIntMap(cellStr);
            }
        }
        return null;
    }

    /**
     * 将<key,value>形式的字符串转为IntTuple
     */
    private StringIntTuple toStringIntTuple(String str) {
        if (str.isEmpty()) {
            return null;
        }
        String key = str.substring(1, str.indexOf(ELEMENT_SPLITER_CHAR));
        int value = Integer.parseInt(str.substring(str.indexOf(ELEMENT_SPLITER_CHAR) + 1, str.length() - 1));
        return new StringIntTuple(key, value);

    }

    /**
     * 将<key,value,probability>形式转成IntThreeTuple
     */
    private ThreeTuple toThreeTuple(String str) {
        if (str.isEmpty()) {
            return null;
        }
        String pair = str.substring(1, str.length() - 1);

        String[] tuples = pair.split(ELEMENT_SPLITER);
        return new ThreeTuple(tuples[0], Integer.valueOf(tuples[1]), Integer.valueOf(tuples[2]));
    }

    /**
     * 将<key,float>形式转成StringFloatTuple
     */
    private StringFloatTuple toStringFloatTuple(String str) {
        if (str.isEmpty()) {
            return null;
        }
        String pair = str.substring(1, str.length() - 1);

        String[] tuples = pair.split(ELEMENT_SPLITER);
        return new StringFloatTuple(tuples[0], Float.valueOf(tuples[1]));
    }

    /**
     * 将<x,y,z><a,b,c>形式的字符串转为List<String>
     */
    private List<List> toListList(String cellStr) {
        List<List> tuples = Lists.newArrayList();
        if (!cellStr.isEmpty()) {
            for (String pair : cellStr.split(GROUP_SPLITER)) {
                pair = pair.substring(1, pair.length() - 1);
                List list = Arrays.asList(pair.split(ELEMENT_SPLITER));
                tuples.add(list);
            }
        }
        return tuples;
    }

    /**
     * 将<key,value><key,value>形式的字符串转为List<IntTuple>
     */
    private List<StringIntTuple> toStringIntTupleList(String str) {
        List<StringIntTuple> tuples = Lists.newArrayList();
        if (!str.isEmpty()) {
            for (String pair : str.split(GROUP_SPLITER)) {
                tuples.add(toStringIntTuple(pair));
            }
        }
        return tuples;
    }

    /**
     * 将<key,value><key,value>形式的字符串转为List<IntTuple>
     */
    private List<IntDoubleTuple> toIntDoubleTupleList(String str) {
        List<IntDoubleTuple> tuples = Lists.newArrayList();
        if (!str.isEmpty()) {
            for (String pair : str.split(GROUP_SPLITER)) {
                tuples.add(toIntDoubleTuple(pair));
            }
        }
        return tuples;
    }

    /**
     * 将<key,value><key,value>形式的字符串转为List<IntTuple>
     */
    private List<IntTuple> toIntTupleList(String str) {
        List<IntTuple> tuples = Lists.newArrayList();
        if (!str.isEmpty()) {
            for (String pair : str.split(GROUP_SPLITER)) {
                tuples.add(toIntTuple(pair));
            }
        }
        return tuples;
    }

    /**
     * 将<123,456,789>形式的字符串转换为List<Integer>
     */
    private List<Integer> toIntList(String str) {
        str = this.stripOffBracketsIfNeed(str);
        List<Integer> ints = Lists.newArrayList();
        if (!str.isEmpty()) {
            for (String intStr : str.split(ELEMENT_SPLITER)) {
                ints.add(Integer.parseInt(intStr));
            }
        }
        return ints;
    }

    /**
     * 将<123,456,789>形式的字符串转换为List<Integer>
     */
    private List<Double> toDoubleList(String str) {
        str = this.stripOffBracketsIfNeed(str);
        List<Double> doubles = Lists.newArrayList();
        if (!str.isEmpty()) {
            for (String intStr : str.split(ELEMENT_SPLITER)) {
                doubles.add(Double.parseDouble(intStr));
            }
        }
        return doubles;
    }

    private String stripOffBracketsIfNeed(String str) {
        if(str.contains("<")) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

    /**
     * 将<key,value,probability>形式转成List<IntThreeTuple>
     */
    private List<ThreeTuple> toThreeTupleList(String str) {
        if (str.isEmpty()) {
            return null;
        }
        List<ThreeTuple> list = new ArrayList<ThreeTuple>();
        for (String pair : str.split(GROUP_SPLITER)) {
            list.add(toThreeTuple(pair));
        }
        return list;
    }

    /**
     * 将<key,float>形式转成List<StringFloatTuple>
     */
    private List<StringFloatTuple> toStringFloatTupleList(String str) {
        if (str.isEmpty()) {
            return null;
        }
        List<StringFloatTuple> list = new ArrayList<StringFloatTuple>();
        for (String pair : str.split(GROUP_SPLITER)) {
            list.add(toStringFloatTuple(pair));
        }
        return list;
    }

    /**
     * 将<x,y,z>形式的字符串转为List<String>
     */
    private List<String> toStringList(String cellStr) {
        cellStr = this.stripOffBracketsIfNeed(cellStr);
        return Arrays.asList(cellStr.split(ELEMENT_SPLITER));
    }

    /**
     * 将123|456|789形式的字符串转换为Set<Integer>
     */
    private Set<Integer> toIntSet(String str) {
        Set<Integer> ints = Sets.newHashSet();
        if (!str.isEmpty()) {
            for (String intStr : str.split(ELEMENT_SPLITER)) {
                ints.add(Integer.parseInt(intStr));
            }
        }
        return ints;
    }

    /**
     * 将123|456|789形式的字符串转换为int[]
     */
    private int[] toIntArray(String str) {
        String[] strArr = str.split(ELEMENT_SPLITER);
        int[] rets = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            try {
                rets[i] = Integer.parseInt(strArr[i]);
            } catch (Exception e) {
                log.error("", e);
                rets[i] = 0;
            }

        }
        return rets;
    }

    private Map toIntIntMap(String str) {
        Map<Integer, Integer> map = Maps.newHashMap();
        if (!str.isEmpty()) {
            for (String pair : str.split(GROUP_SPLITER)) {
                int key = Integer.parseInt(pair.substring(1, pair.indexOf(ELEMENT_SPLITER_CHAR)));
                int value = Integer.parseInt(pair.substring(pair.indexOf(ELEMENT_SPLITER_CHAR) + 1, pair.length() - 1));
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * 将<key,value>形式的字符串转为IntTuple
     */
    private IntDoubleTuple toIntDoubleTuple(String str) {
        if (str.isEmpty()) {
            return null;
        }
        int key = Integer.parseInt(str.substring(1, str.indexOf(ELEMENT_SPLITER_CHAR)));
        double value = Double.parseDouble(str.substring(str.indexOf(ELEMENT_SPLITER_CHAR) + 1, str.length() - 1));
        return new IntDoubleTuple(key, value);
    }

    /**
     * 将<key|value>形式的字符串转为IntTuple
     */
    private IntTuple toIntTuple(String str) {
        if (str.isEmpty()) {
            return null;
        }
        int key = Integer.parseInt(str.substring(1, str.indexOf(ELEMENT_SPLITER_CHAR)));
        int value = Integer.parseInt(str.substring(str.indexOf(ELEMENT_SPLITER_CHAR) + 1, str.length() - 1));
        return new IntTuple(key, value);

    }
}