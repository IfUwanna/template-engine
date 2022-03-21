package com.template.core;

import com.template.exception.TemplateException;
import com.template.resolver.Resolver;
import com.template.util.TemplateUtil;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.io.Writer;

import static com.template.core.Template.getTemplateFullPath;

/**
 * packageName    : com.template.core
 * fileName       : UserTemplate
 * author         : Jihun Park
 * date           : 2022/03/18
 * description    : User Template
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/03/18        Jihun Park       최초 생성
 */
public class UserTemplate implements Template {

    private String templatePath;        //템플릿 경로
    private String template;            //템플릿
    private String result;              //변환 결과
    private Resolver resolver;          //Data Resolver
    private JSONArray data = new JSONArray();

    public UserTemplate(String templateName) {
        this.templatePath = getTemplateFullPath(templateName);
        this.template = TemplateUtil.readTemplate(templatePath);   //read Template file
    }

    public UserTemplate(String templateName, Resolver resolver) {
        this.templatePath = getTemplateFullPath(templateName);
        this.template = TemplateUtil.readTemplate(templatePath);    //read Template file
        this.resolver = resolver;
        this.data = TemplateUtil.readData(resolver);    //read data
    }

    @Override
    public void readData(Resolver resolver) {
        this.data = TemplateUtil.readData(resolver);
    }

    @Override
    public void convert() {
        result = TemplateParser.convertTemplate(template, data);
        if(result.lastIndexOf("\n") == result.length()-1){
            result =  result.substring(0,result.length()-1);
        }
    }

    @Override
    public void write(Writer writer) {
        try {
            writer.write(result);
            System.out.println("성공적으로 출력 되었습니다. : "  + writer.getClass().getSimpleName());
            writer.close();
        } catch (IOException e) {
            throw new TemplateException("결과 출력 중 오류가 발생하였습니다.: " + e.getMessage());
        }
    }

    public String getTemplatePath(String templatePath) {
        return this.templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = getTemplatePath(templatePath);
        this.template = TemplateUtil.readTemplate(templatePath);
    }

    public Resolver getResolver() {
        return resolver;
    }

    public void setResolver(Resolver resolver) {
        this.resolver = resolver;
        this.data = TemplateUtil.readData(resolver);
    }


}
