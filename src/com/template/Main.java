package com.template;

import com.template.core.Template;
import com.template.core.UserTemplate;
import com.template.resolver.FileResolver;
import com.template.util.PropertieUtil;
import com.template.util.TemplateUtil;

import java.io.PrintWriter;

import static com.template.constant.Propertie.DATA;
import static com.template.constant.Propertie.TEMPLATE;

public class Main {

    public static void main(String[] args) {

        // option (1st args > 2nd:properties)
        boolean hasArgs = args.length==2;
        String templateName = hasArgs? args[0] : PropertieUtil.getValue(TEMPLATE.getKey());
        String data = hasArgs? args[1] : PropertieUtil.getValue(DATA.getKey());

        // initialize Template
        Template template = new UserTemplate(templateName, new FileResolver(data));  // initialize Template(template + File data)
//        Template template = new UserTemplate(templateName, new URIResolver(data)); // initialize Template(template + URI data)

        // convert Data
        template.convert();

        // write
        template.write(TemplateUtil.getDefaultFileWriter()); // write file
        template.write(new PrintWriter(System.out)); // write System

    }



}
