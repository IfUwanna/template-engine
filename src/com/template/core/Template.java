package com.template.core;

import com.template.util.PropertieUtil;

import java.io.Writer;

import static com.template.constant.Propertie.PROPERTIE_PREFIX_TEMPLATE;

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
     * methodName : init
     * author : Jihun Park
     * description : initialize Template
     */
    void init();

    /**
     * methodName : convert
     * author : Jihun Park
     * description : convert data
     */
    void convert();

    /**
     * methodName : write
     * author : Jihun Park
     * description : writer contents
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
        return PropertieUtil.getInstance().getValue(PROPERTIE_PREFIX_TEMPLATE.getKey()) + templateName;
    }

}
