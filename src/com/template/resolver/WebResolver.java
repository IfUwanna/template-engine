package com.template.resolver;

import com.template.exception.TemplateException;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * packageName    : com.template.resolver
 * fileName       : WebResolver
 * author         : Jihun Park
 * date           : 2022/03/18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/03/18        Jihun Park       최초 생성
 */
public class WebResolver implements Resolver {

    private String resource;

    public WebResolver(String resource) {
        this.resource = resource;
    }

    @Override
    public Reader getReader(){
        try{
            URI uri = new URI(resource);
            URL url = uri.toURL();
            return new InputStreamReader(url.openStream(), "UTF-8");
        }catch(URISyntaxException e){
            throw new TemplateException("유효하지 않은 URI입니다.: " + resource);
        }catch(IOException e){
            throw new TemplateException("URI를 읽어오는 중 오류가 발생하였습니다. " + resource);
        }

    }
}
