package com.template;

import com.template.core.Template;
import com.template.core.UserTemplate;
import com.template.resolver.FileResolver;
import com.template.resolver.WebResolver;
import com.template.util.TemplateProperties;

import java.io.PrintWriter;

import static com.template.PropertieType.*;

public class Main {

    public static void main(String[] args) {

        boolean hasArgs = args.length==2;
        String templateName = hasArgs? args[0] : TemplateProperties.getInstance().getValue(PROPERTIE_TEMPLATE.getKey());
        String data = hasArgs? args[1] : TemplateProperties.getInstance().getValue(PROPERTIE_DATA.getKey());

        Template template = new UserTemplate(templateName, new FileResolver(data));  // user > load File TODO service 추상화?
        //Template template = new UserTemplate(templateName, new WebResolver(data));     //  user > load URI
        template.init();    //초기화
        template.convert();
        template.write(new PrintWriter(System.out));

    }



}
