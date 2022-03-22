package com.template;

import com.template.core.Template;
import com.template.core.DefaultTemplate;
import com.template.resolver.FileResolver;
import com.template.resolver.URIResolver;
import com.template.util.PropertieUtil;
import com.template.util.TemplateUtil;

import java.io.PrintWriter;

import static com.template.constant.Propertie.*;

public class Main {

    public static void main(String[] args) {

        // 0. Operation Option (1st args > 2nd:properties)
        String templateName =  (args.length<=2||args[0].isBlank())? PropertieUtil.getValue(TEMPLATE.getKey()):args[0];
        String data = (args.length<=2||args[1].isBlank())?  PropertieUtil.getValue(DATA.getKey()) : args[1];

        // 1. Initialize Template
        Template template = new DefaultTemplate(templateName, new FileResolver(data));  // initialize Template(template + File data)
//        Template template = new DefaultTemplate(templateName, new URIResolver(PropertieUtil.getValue(DATA_URI.getKey()))); // initialize Template(template + URI data) 확장 샘플
//        Template template = new DefaultTemplate(templateName);     // initialize Template (template only)
//        template.readData(new FileResolver(data));              // read Data (File data)

        // 2. Convert Data (Using Template)
        template.convert();

        // 3. Write
        template.write(TemplateUtil.getDefaultFileWriter()); // write File
        template.write(new PrintWriter(System.out));         // write System(참고용)

    }
}
