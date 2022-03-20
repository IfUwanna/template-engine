package com.template.util;

import com.template.exception.TemplateException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * packageName    : com.template.util
 * fileName       : PropertieUtil
 * author         : Jihun Park
 * date           : 2022/03/18
 * description    : Propertie Util (singleton)
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/03/18        Jihun Park       최초 생성
 */
public class PropertieUtil {

    private static PropertieUtil propertieUtil;
    private static Properties properties;
    private static final String PROPERTIES_PATH = "./template.properties";

    public static PropertieUtil getInstance() {
        if (propertieUtil == null) {
            propertieUtil = new PropertieUtil();
        }
        return propertieUtil;
    }

    private PropertieUtil() {
        FileInputStream fis = null;

        try {
            properties = new Properties();
            fis = new FileInputStream(PROPERTIES_PATH);
            properties.load(fis);
        } catch (Exception e) {
            throw new TemplateException("properties 파일 초기화중 오류가 발생하였습니다.");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) { }
            }
        }
    }

    public static String getValue(String name) {
        String value = getInstance().properties == null? "" : getInstance().properties.getProperty(name);
        return value;
    }
/*
    public int getIntValue(String name) {
        int returnValue = 0;
        String strValue = this.getValue(name);
        if (strValue != null && strValue.length() > 0) {
            returnValue = Integer.parseInt(strValue);
        }
        return returnValue;
    }*/
}
