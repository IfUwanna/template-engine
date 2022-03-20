package com.template.util;

import com.template.constant.Syntax;
import com.template.exception.TemplateException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

    public static String convertTemplate(String template, JSONArray jsonArray) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < jsonArray.size(); i++) {

            //JSONObject user = (JSONObject) users.get(i);
            String temp = template;

            // expression pointers
            int start = temp.indexOf(Syntax.PREFIX.getEx());
            int end = temp.indexOf(Syntax.SUFFIX.getEx(), start) + Syntax.SUFFIX.getExLength();

            while(start != -1){

                // extract template expression
                String templateEx = temp.substring(start,end);

                if(templateEx.indexOf(Syntax.ITERATION_PREFIX.getEx()) > -1){  /* iteration operation */

                    // extract iteration expression
                    int forEnd = temp.indexOf(Syntax.SUFFIX.getEx(), temp.indexOf(Syntax.ITERATION_SUFFIX.getEx(), start)) + Syntax.SUFFIX.getExLength(); // 처음 만나는 endfor부터 시작해서 첫 괄호닫히는 부분 +2칸까지 하면 for 통째로
                    String forEx = temp.substring(start, forEnd);

                    // extract iteration info
                    String targetName = templateEx.split(" ")[2].trim();       // targetName : ADDR
                    String targetKey = templateEx.split(" ")[4].trim();        // targetKey : USER.info.addrs
                    int innerEnd = temp.indexOf(Syntax.ITERATION_SUFFIX.getEx(), start);
                    String innerTemplate = temp.substring(end, innerEnd);   // innerText

                    // get iteration value
                    Object value = getValue(jsonArray.get(i), targetKey);
                    JSONArray arrValue = (JSONArray) value;
                    StringBuilder allArrayResult = new StringBuilder();
                    for (int j = 0; j < arrValue.size(); j++) {// array별
                        String innerTemp = innerTemplate;
                        String innerEx = TemplateUtil.extractExpression(innerTemplate);
                        while(!innerEx.isEmpty()){
                            JSONObject jsonObject = (JSONObject) arrValue.get(j);
                            String replaceKey = innerEx.substring(3, innerEx.length() - Syntax.SUFFIX.getExLength()).trim();
                            Object innerValue = getValue(jsonObject, replaceKey);
                            innerTemp = innerTemp.replace(innerEx,(String)innerValue);
                            innerEx = TemplateUtil.extractExpression(innerTemp);
                        }
                        // 하나의 배열 결과 기록
                        allArrayResult.append(innerTemp);
                    }
                    // replace template
                    temp = temp.replace(forEx,allArrayResult.toString());

                }else{  /* replace operation */

                    // extract replaceKey
                    String replaceKey = templateEx.substring(3, templateEx.length() - Syntax.SUFFIX.getExLength()).trim();

                    // get replace value
                    Object value = getValue(jsonArray.get(i), replaceKey);

                    // replace template
                    temp = temp.replace(templateEx,(String)value);
                }
                // move next expression
                start = temp.indexOf(Syntax.PREFIX.getEx());
                end = temp.indexOf(Syntax.SUFFIX.getEx(),start) + Syntax.SUFFIX.getExLength();
            }
            // append result
            sb.append(temp.replace("\\n",System.lineSeparator()));
        }
        // result
        return sb.toString();
    }


    public static String extractExpression(String template){

        try{
            int start = template.indexOf(Syntax.PREFIX.getEx());
            int end = template.indexOf(Syntax.SUFFIX.getEx(), start) + Syntax.SUFFIX.getExLength();
            if(start < 0) return "";
            return template.substring(start,end);

        }catch(Exception e){
            throw new TemplateException("표현식을 추출할 수 없습니다. : " + e.getMessage());
        }
    }

    public static Object getValue(Object obj, String Key) {

        String[] key = Key.split("\\.");

        for (int j = 1; j < key.length; j++) {
            if (obj instanceof JSONArray) {
                JSONArray jsonArray =(JSONArray) obj;
                int index = Integer.valueOf(key[j]);
                if(jsonArray.size() > index){
                    obj =  jsonArray.get(index);
                }else{
                    obj = ""; break;
                }
            }else if (obj instanceof JSONObject) {
                obj = ((JSONObject) obj).get(key[j]);
            }
        }
        if(obj == null)throw new TemplateException("유효하지 않은 변환 식입니다. : "  + Key);

        return obj;
    }
}
