package com.template.util;

import com.template.exception.TemplateException;
import com.template.resolver.Resolver;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.stream.Collectors;

import static com.template.constant.Propertie.PREFIX_RESULT;
import static com.template.constant.Propertie.RESULT;

/**
 * packageName    : com.template.util
 * fileName       : TemplateUtil
 * author         : Jihun Park
 * date           : 2022/03/20
 * description    : Template Util
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/03/20        Jihun Park       최초 생성
 */
public class TemplateUtil {

    /**
     * methodName : read Template
     * author : Jihun Park
     * description : read template file
     */
    public static String readTemplate(String templatePath){
        try{
            BufferedReader bf = new BufferedReader(new FileReader(templatePath));
            String template =  bf.lines().collect(Collectors.joining());
            bf.close();
            return template;
        }catch(FileNotFoundException e){
            throw new TemplateException("템플릿 파일을 찾을 수 없습니다.: " + templatePath);
        } catch (IOException e) {
            throw new TemplateException("템플릿 파일을 읽어들이는 중 오류가 발생하였습니다.: " + templatePath);
        }
    }

    /**
     * methodName : readData
     * author : Jihun Park
     * description : read Data(Json)
     */
    public static JSONArray readData(Resolver resolver){

        try {
            JSONArray data = new JSONArray();
            Object obj = new JSONParser().parse(resolver.getReader());
            if (obj instanceof JSONArray) {
                data = (JSONArray)obj;
            }else if (obj instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject)obj;
                data.add(jsonObject);
            }
            return data;
        } catch (IOException e) {
            throw new TemplateException("data 파일을 읽어들이는 중 오류가 발생하였습니다. : " + e.getMessage());
        } catch (ParseException e) {
            throw new TemplateException("data 파일 파싱 중 오류가 발생하였습니다. : " + e.getMessage());
        }
    }

    /**
     * methodName : getDefaultFileWriter
     * author : Jihun Park
     * description : get default file writer
     * @return fileWriter
     */
    public static FileWriter getDefaultFileWriter(){

        try {
            File file = new File(PropertieUtil.getValue(PREFIX_RESULT.getKey()) + PropertieUtil.getValue(RESULT.getKey()));
            FileWriter fileWriter = new FileWriter(file);
            return fileWriter;
        } catch (IOException e) {
            throw new TemplateException("파일 쓰기 준비 중 오류가 발생하였습니다.  : "  + e.getMessage());
        }
    }

}
