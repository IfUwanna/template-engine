package com.template.resolver;

import com.template.exception.TemplateException;
import com.template.util.PropertieUtil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import static com.template.constant.Propertie.PREFIX_DATA;

/**
 * packageName    : com.template.resolver
 * fileName       : FileResolver
 * author         : Jihun Park
 * date           : 2022/03/18
 * description    : File Resolver
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/03/18        Jihun Park       최초 생성
 */
public class FileResolver implements Resolver {

    private String data;

    public FileResolver(String data) {
        this.data = PropertieUtil.getValue(PREFIX_DATA.getKey()) + data;
    }

    @Override
    public Reader getReader()  {
        try{
            return new FileReader(data);
        }catch(FileNotFoundException e){
            throw new TemplateException("Data 파일을 찾을 수 없습니다.: " + data);
        }
    }
}
