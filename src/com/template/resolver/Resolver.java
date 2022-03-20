package com.template.resolver;

import com.template.util.PropertieUtil;

import java.io.Reader;

import static com.template.constant.Propertie.PROPERTIE_PREFIX_DATA;

/**
 * packageName    : com.template.resolver
 * fileName       : BaseResolver
 * author         : Jihun Park
 * date           : 2022/03/18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/03/18        Jihun Park       최초 생성
 */
public interface Resolver {

    Reader getReader();

    /**
     * methodName : getDataFullPath
     * author : Jihun Park
     * description :
     * @param data
     * @return string
     */
    static String getDataFullPath(String data){
        return PropertieUtil.getInstance().getValue(PROPERTIE_PREFIX_DATA.getKey()) + data;
    }
}
