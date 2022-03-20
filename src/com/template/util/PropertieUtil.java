package com.template.util;

import com.template.constant.Propertie;
import com.template.exception.TemplateException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.template.constant.Propertie.*;

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

    private PropertieUtil() {
        FileInputStream fis = null;

        try {
            properties = new Properties();
            fis = new FileInputStream(PATH.getKey());
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

    /**
     * methodName : getInstance
     * author : Jihun Park
     * description : singleton method
     * @return propertie util
     */
    public static PropertieUtil getInstance() {
        if (propertieUtil == null) {
            propertieUtil = new PropertieUtil();
        }
        return propertieUtil;
    }

    /**
     * methodName : getValue
     * author : Jihun Park
     * description : get Propertie value
     * @param name
     * @return string
     */
    public static String getValue(String name) {
        String value = getInstance().properties == null? "" : getInstance().properties.getProperty(name);
        return value;
    }

}
