package com.template.core;

import com.template.exception.TemplateException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static com.template.constant.Syntax.*;

/**
 * packageName    : com.template.core
 * fileName       : TemplateParser
 * author         : Jihun Park
 * date           : 2022/03/21
 * description    : Template Parser
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/03/21        Jihun Park       최초 생성
 */
public class TemplateParser {

    /**
     * methodName : convertTemplate
     * author : Jihun Park
     * description : convert Template (check TopTemplate and routing)
     * @param template
     * @param jsonArray
     * @return string
     */
    public static String convertTemplate(String template, JSONArray jsonArray) {

        if(isTopTemplate(template)){
            return convertTopTemplate(template,jsonArray); // Top Template
        }else{
            return convertEachTemplate(template,jsonArray); // Each Template
        }
    }

    /**
     * methodName : convertTopTemplate
     * author : Jihun Park
     * description : convert Template (Top Level)
     * @param template
     * @param jsonArray
     * @return string
     */
    public static String convertTopTemplate(String template, JSONArray jsonArray) {

        // extract expression
        String expression = TemplateParser.extractIterationExpression(template);

        while(!expression.isEmpty()){

            // extract iteration expression
            String forExpression = extractForExpression(template);

            // extract innerTemplate
            String innerTemplate = extractForInnerTemplate(forExpression);

            // get iteration value
            String targetKey = expression.split(" ")[4].trim();   // USERS.*.membership.id
            StringBuilder result = new StringBuilder();

            // convert innerTemplate
            for (int i = 0; i < jsonArray.size() ; i++) {
                Object value = getValue(jsonArray, targetKey.replace("*", String.valueOf(i)));  // USERS.*.membership.id >  USERS.[index].membership.id

                if(value instanceof JSONArray) {  // collection >> convertEachTemplate() recursive call   ex) USERS.*.info.addrs
                    result.append(convertEachTemplate(innerTemplate, (JSONArray)value));
                }else{      // single object >> replace values ex) USERS.*.membership.id
                    result.append(innerTemplate.replace(TemplateParser.extractExpression(innerTemplate),(String)value));
                }
            }
            template = template.replace(forExpression,result.toString());

            // move next iterationExpression
            expression = TemplateParser.extractIterationExpression(template);
        }

        template = template.replace("\\n",System.lineSeparator());
        return template;
    }

    /**
     * methodName : convertEachTemplate
     * author : Jihun Park
     * description :  convert Template (Each Element)
     * @param template
     * @param jsonArray
     * @return string
     */
    public static String convertEachTemplate(String template, JSONArray jsonArray) {

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < jsonArray.size(); i++) {
            result.append(convertEachTemplate(template, (JSONObject) jsonArray.get(i)));
        }
        return result.toString();
    }

    /**
     * methodName : convertEachTemplate
     * author : Jihun Park
     * description :  convert Template (Each Element)
     * @param template
     * @param jsonObject
     * @return string
     */
    public static String convertEachTemplate(String template, JSONObject jsonObject) {

        // extract expression
        String expression = TemplateParser.extractExpression(template);

        while(!expression.isEmpty()){

            if(expression.indexOf(REPLACE_PREFIX.getEx()) > -1){    /*== replace operation ==*/

                // extract replaceKey
                String replaceKey = expression.substring(3, expression.length() - SUFFIX.getExLength()).trim();

                // get replace value
                Object value = getValue(jsonObject, replaceKey);

                // replace template
                template = template.replace(expression,(String)value);

            }else if(expression.indexOf(ITERATION_PREFIX.getEx()) > -1){  /*== iteration operation ==*/

                // extract iteration expression
                String forExpression = extractForExpression(template);

                // extract innerTemplate
                String innerTemplate = extractForInnerTemplate(forExpression);

                // get iteration value
                String targetKey = expression.split(" ")[4].trim();   // targetKey : USER.info.addrs
                Object value = getValue(jsonObject, targetKey);             // addr[]
                JSONArray arrValue = (JSONArray) value;

                // convert innerTemplate (recursion)
                StringBuilder innerResult = new StringBuilder();
                innerResult.append(convertEachTemplate(innerTemplate,arrValue));

                // replace template
                template = template.replace(forExpression,innerResult.toString());
            }

            // next expression
            expression = TemplateParser.extractExpression(template);
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
    private static boolean isTopTemplate(String template){

        int start = template.indexOf(ITERATION_PREFIX.getEx());
        int end = template.indexOf(SUFFIX.getEx(), start) + SUFFIX.getExLength();

        while(start > -1) {
            String iterationExpression = template.substring(start,end);
            String targetKey = iterationExpression.split(" ")[4].trim();   // targetKey : USERS.*.membership.id
            if("*".equals(targetKey.split("\\.")[1])) { // USERS 단위 변환 표현식
                return true;
            }
            start = template.indexOf(ITERATION_PREFIX.getEx(), start + 1);
            end = template.indexOf(SUFFIX.getEx(), start) + SUFFIX.getExLength();
        }
        return false;

    }
    /**
     * methodName : extractExpression
     * author : Jihun Park
     * description : extract Expression <? ~ ?>
     * @param template
     * @return string
     */
    private static String extractExpression(String template){

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
     * methodName : extractIterationExpression
     * author : Jihun Park
     * description : extract iteration expression <? for ~ ?>
     * @param template
     * @return string
     */
    private static String extractIterationExpression(String template){

        try{
            int start = template.indexOf(ITERATION_PREFIX.getEx());
            int end = template.indexOf(SUFFIX.getEx(), start) + SUFFIX.getExLength();
            if(start < 0) return "";
            return template.substring(start,end);

        }catch(Exception e){
            throw new TemplateException("표현식 추출 중 오류가 발생하였습니다. : " + e.getMessage());
        }
    }

    /**
     * methodName : extractForExpression
     * author : Jihun Park
     * description : extract for expression  <? for ~ ?> [innerTemplate] <? endfor ?>
     * @param template
     * @return string
     */
    private static String extractForExpression(String template){

        try{
            int forStart = template.indexOf(ITERATION_PREFIX.getEx());
            int forEnd = template.indexOf(ITERATION_SUFFIX.getEx(), forStart) + ITERATION_SUFFIX.getExLength();
            if(forStart < 0) return "";
            return template.substring(forStart, forEnd);
        }catch(Exception e){
            throw new TemplateException("표현식 추출 중 오류가 발생하였습니다. : " + e.getMessage());
        }
    }

    /**
     * methodName : extractForInnerTemplate
     * author : Jihun Park
     * description : extract for InnerTemplate
     * @param forExpression
     * @return string
     */
    private static String extractForInnerTemplate(String forExpression){

        try{

            int innerTemplateStart = forExpression.indexOf(SUFFIX.getEx()) + SUFFIX.getExLength();
            int innerTemplateEnd = forExpression.indexOf(ITERATION_SUFFIX.getEx());
            if(innerTemplateStart < 0) return "";
            return forExpression.substring(innerTemplateStart, innerTemplateEnd);

        }catch(Exception e){
            throw new TemplateException("표현식 추출 중 오류가 발생하였습니다. : " + e.getMessage());
        }
    }


    /**
     * methodName : getValue
     * author : Jihun Park
     * description : JSONObject/JSONArray to value
     * @param obj
     * @param key
     * @return object (last Depth String value)
     */
    private static Object getValue(Object obj, String key) {

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
            if (obj == null) throw new TemplateException("유효하지 않은 변환식입니다. : " + key);
            return obj;
        }catch(Exception e){
            throw new TemplateException("데이터 변환 중 오류가 발생하였습니다. " + e.getMessage());
        }
    }



}
