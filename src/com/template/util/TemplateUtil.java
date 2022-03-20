package com.template.util;

import com.template.constant.Syntax;
import com.template.exception.TemplateException;
import com.template.resolver.Resolver;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.stream.Collectors;

import static com.template.constant.Propertie.*;
import static com.template.constant.Syntax.*;

/**
 * packageName    : com.template.util
 * fileName       : TemplateUtil
 * author         : Jihun Park
 * date           : 2022/03/20
 * description    :
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


    public static String convertTemplate(String template, JSONArray jsonArray) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jsonArray.size(); i++) {
            sb.append(convertTemplate(template, (JSONObject) jsonArray.get(i)));
        }
        return sb.toString();
    }

    public static String convertTemplate(String template, JSONObject jsonObject) {

        // extract expression
        String expression = TemplateUtil.extractExpression(template);

        while(!expression.isEmpty()){

            if(expression.indexOf(ITERATION_PREFIX.getEx()) > -1){  /*== iteration operation ==*/

                int start = template.indexOf(PREFIX.getEx());
                int end = template.indexOf(SUFFIX.getEx(), start) + SUFFIX.getExLength();

                // extract iteration expression
                int forEnd = template.indexOf(ITERATION_SUFFIX.getEx(),start) + ITERATION_SUFFIX.getExLength();
                String forExpression = template.substring(start, forEnd); // 반복문 전체 추출

                // extract innerTemplate
                int innerTemplateEnd = template.indexOf(ITERATION_SUFFIX.getEx(), start);
                String innerTemplate = template.substring(end, innerTemplateEnd);

                // get iteration value
                String targetKey = expression.split(" ")[4].trim();   // targetKey : USER.info.addrs > addrs[]
                Object value = getValue(jsonObject, targetKey);
                JSONArray arrValue = (JSONArray) value;

                // convert innerTemplate (recursion)
                StringBuilder innerResult = new StringBuilder();
                innerResult.append(convertTemplate(innerTemplate,arrValue));

                // replace template
                template = template.replace(forExpression,innerResult.toString());

            }else{  /*== replace operation ==*/

                // extract replaceKey
                String replaceKey = expression.substring(3, expression.length() - SUFFIX.getExLength()).trim();

                // get replace value
                Object value = getValue(jsonObject, replaceKey);

                // replace template
                template = template.replace(expression,(String)value);
            }
            // next expression
            expression = TemplateUtil.extractExpression(template);
        }

        template = template.replace("\\n",System.lineSeparator());
        // return result
        return template;
    }


    /**
     * methodName : extractExpression
     * author : Jihun Park
     * description : extract Expression <? ~ ?>
     * @param template
     * @return string
     */
    public static String extractExpression(String template){

        try{
            int start = template.indexOf(PREFIX.getEx());
            int end = template.indexOf(SUFFIX.getEx(), start) + SUFFIX.getExLength();
            if(start < 0) return "";
            return template.substring(start,end);

        }catch(Exception e){
            throw new TemplateException("표현식 추출 중 오류가 발생하였습니다. : " + e.getMessage());
        }
    }

    /**
     * methodName : getValue
     * author : Jihun Park
     * description :
     * @param obj
     * @param key
     * @return object
     */
    public static Object getValue(Object obj, String key) {

        try {
            String[] keys = key.split("\\.");

            for (int j = 1; j < keys.length; j++) {
                if (obj instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) obj;
                    int index = Integer.valueOf(keys[j]);
                    if (jsonArray.size() > index) {
                        obj = jsonArray.get(index);
                    } else {
                        obj = "";
                        break;
                    }
                } else if (obj instanceof JSONObject) {
                    obj = ((JSONObject) obj).get(keys[j]);
                }
            }
            if (obj == null) throw new TemplateException("유효하지 않은 변환식입니다. : " + keys);
            return obj;
        }catch(Exception e){
            throw new TemplateException("데이터 변환 중 오류가 발생하였습니다. : " + e.getMessage());
        }

    }

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
