package com.template.core;

import com.template.domain.User;
import com.template.exception.TemplateException;
import com.template.resolver.Resolver;
import com.template.util.JsonUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.template.core.Template.*;

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
    private String contents;            //변환 텍스트
    private Resolver resolver;          //data resolver
    private JSONArray users = new JSONArray(); // data

    public UserTemplate(String templateName, Resolver dataResolver) {
        this.templatePath = getTemplateFullPath(templateName);
        this.resolver = dataResolver;
    }

    @Override
    public void init() {
        this.readTemplate();    // read template file
        this.readData();        // read data
    }

    @Override
    public void convert() {

        StringBuilder sb = new StringBuilder();

        String REPLACE_PREFIX = "<?=";
        String REPLACE_SUFFIX = "?>";

        for (int i = 0; i < users.size(); i++) {

            JSONObject user = (JSONObject) users.get(i);
            String temp = template;

            // pointers
            int start = temp.indexOf(REPLACE_PREFIX);
            int end = temp.indexOf(REPLACE_SUFFIX, start);

            while(start != -1){

                // extract replaceStr
                String replaceStr = temp.substring(start,end+2);
                String[] arr = replaceStr.substring(3,replaceStr.length()-2).split("\\.");

                // get value
                Object obj = user.get(arr[1]);
                for (int j = 2; j < arr.length; j++) {
                    if (obj instanceof JSONArray) {
                        JSONArray jsonArray =(JSONArray) obj;
                        int index = Integer.valueOf(arr[j].trim());
                        if(jsonArray.size() > index){
                            obj =  jsonArray.get(index);
                        }else{
                            obj = ""; break;
                        }
                    }else if (obj instanceof JSONObject) {
                        obj = ((JSONObject) obj).get(arr[j].trim());
                    }
                }
                // replace value
                temp = temp.replace(replaceStr,(String)obj);

                // move pointers
                start = temp.indexOf(REPLACE_PREFIX);
                end = temp.indexOf(REPLACE_SUFFIX, start);
            }
            // append result
            sb.append(temp.replace("\\n",System.lineSeparator()));
        }
        // result
        contents = sb.toString();
    }

    @Override
    public void write(Writer writer) {
        try {
            writer.write(contents);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * methodName : read Template
     * author : Jihun Park
     * description : read Template
     */
    private void readTemplate(){
        try{
            BufferedReader bf = new BufferedReader(new FileReader(templatePath));
            template =  bf.lines().collect(Collectors.joining());
            bf.close();
        }catch(FileNotFoundException e){
            throw new TemplateException("템플릿 파일을 찾을 수 없습니다.: " + templatePath);
        } catch (IOException e) {
            throw new TemplateException("템플릿 파일을 읽어들이는 중 오류가 발생하였습니다.: " + templatePath);
        }
    }

    /**
     * methodName : readData
     * author : Jihun Park
     * description : read Data
     */
    private void readData(){

        try {
            Object obj = new JSONParser().parse(resolver.getReader());
            if (obj instanceof JSONArray) {
                this.users = (JSONArray)obj;
            }else if (obj instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject)obj;
                users.add(jsonObject);
            }
        } catch (IOException e) {
            throw new TemplateException("data 파일을 읽어들이는 중 오류가 발생하였습니다. : " + e.getMessage());
        } catch (ParseException e) {
            throw new TemplateException("data 파일 파싱 중 오류가 발생하였습니다. : " + e.getMessage());
        }
    }


    public String getTemplatePath(String templatePath) {
        return this.templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = getTemplatePath(templatePath);
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Resolver getResolver() {
        return resolver;
    }

    public void setResolver(Resolver resolver) {
        this.resolver = resolver;
    }

    public JSONArray getJsonArray() {
        return users;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.users = jsonArray;
    }



}
