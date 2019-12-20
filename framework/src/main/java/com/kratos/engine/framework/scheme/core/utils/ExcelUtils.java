package com.kratos.engine.framework.scheme.core.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * excel工具类
 *
 * @author herton
 */
public class ExcelUtils {
    private static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    public final static String jsTemplate = "excel2js.ftl";
    public final static String javaTemplate = "excel2java.ftl";

    public static String getStringValue(XSSFCell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case HSSFCell.CELL_TYPE_NUMERIC:
                DecimalFormat df = new DecimalFormat("0.00");
                String str = df.format(cell.getNumericCellValue()) + "";
                if (str.endsWith(".00")) {
                    return str.substring(0, str.length() - 3);
                } else {
                    return str;
                }

            default:
                return cell.getStringCellValue();
        }
    }

    public static boolean isEnd(XSSFRow row) {
        if (row == null) {
            return true;
        }
        String str = getStringValue(row.getCell((short) 0, Row.RETURN_NULL_AND_BLANK));
        return str.isEmpty();
    }

    public static List<List<String>> readExcel(File file, List<ExcelColumn> fields) {
        FileInputStream in = null;
        List<List<String>> result = new ArrayList<>();
        List<String> rowList;
        try {
            in = new FileInputStream(file);
            XSSFSheet sheet = new XSSFWorkbook(in).getSheetAt(0);
            for (int rowNum = 0; true; rowNum++) {
                XSSFRow row = sheet.getRow(rowNum);
                if (row == null) break;
                rowList = new ArrayList<>();
                result.add(rowList);
                String str = getStringValue(row.getCell((short) 0, Row.RETURN_NULL_AND_BLANK));
                if (StringUtils.isEmpty(str.isEmpty())) break;
                if(fields != null) {
                    for (ExcelColumn field : fields) {
                        String cellString = getStringValue(row.getCell(field.getColumnIndex(), Row.RETURN_NULL_AND_BLANK));
                        rowList.add(cellString);
                    }
                } else {
                    for (int cellNum = 0; true; cellNum++) {
                        String cellString = getStringValue(row.getCell(cellNum, Row.RETURN_NULL_AND_BLANK));
                        if (StringUtils.isEmpty(cellString)) break;
                        rowList.add(cellString);
                    }
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return result;
    }
}