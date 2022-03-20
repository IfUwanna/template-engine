package com.template.core;

import com.template.constant.Propertie;
import com.template.resolver.Resolver;
import com.template.util.PropertieUtil;

import java.io.Writer;

import static com.template.constant.Propertie.*;

/**
 * packageName    : com.template.core
 * fileName       : BaseTemplate
 * author         : Jihun Park
 * date           : 2022/03/18
 * description    : Template interface
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/03/18        Jihun Park       최초 생성
 */
public interface Template {

    /**
     * methodName : readData
     * author : Jihun Park
     * description : read data
     * @param resolver
     */
    void readData(Resolver resolver);

    /**
     * methodName : convert
     * author : Jihun Park
     * description : convert data
     */
    void convert();

    /**
     * methodName : write
     * author : Jihun Park
     * description : writer result
     * @param writer
     */
    void write(Writer writer);

    /**
     * methodName : getTemplateFullPath
     * author : Jihun Park
     * description : get TemplatePath
     * @param templateName
     * @return string
     */
    static String getTemplateFullPath(String templateName){
        return PropertieUtil.getValue(PREFIX_TEMPLATE.getKey()) + templateName;
    }

}
