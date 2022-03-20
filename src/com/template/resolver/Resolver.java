package com.template.resolver;

import java.io.Reader;

/**
 * packageName    : com.template.resolver
 * fileName       : BaseResolver
 * author         : Jihun Park
 * date           : 2022/03/18
 * description    : Resolver (to read data)
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/03/18        Jihun Park       최초 생성
 */
public interface Resolver {

    /**
     * methodName : getReader
     * author : Jihun Park
     * description : get Reader
     * @return reader
     */
    Reader getReader();

}
