package com.yonyougov.http.util;

import com.yonyougov.http.entity.InterfaceNode;
import com.yonyougov.http.repo.InterfaceRepo;
import groovy.lang.GroovyClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * @ClassName RegisUtil
 * @Description TODO
 * @Author playboy
 * @Date 2020/9/24 10:46 上午
 * @Version 1.0
 **/
public class RegisUtil {
    private static Logger log = LoggerFactory.getLogger(RegisUtil.class);

    static final String JAVA_SOURCE_CODE_JSON = "package com.yonyougov.http.registered;\n" +
            "\n" +
            "import com.yonyougov.http.util.WebSocketUtil;\n" +
            "import org.slf4j.Logger;\n" +
            "import org.slf4j.LoggerFactory;\n" +
            "import org.springframework.web.bind.annotation.RequestMapping;\n" +
            "import org.springframework.web.bind.annotation.RestController;\n" +
            "\n" +
            "import javax.servlet.http.HttpServletRequest;\n" +
            "import javax.servlet.http.HttpServletResponse;\n" +
            "import java.util.Map;\n" +
            "\n" +
            "@RestController\n" +
            "public class SERVICE_NAMEJsonTemplateController {\n" +
            "    private static Logger log = LoggerFactory.getLogger(SERVICE_NAMEJsonTemplateController.class);\n" +
            "\n" +
            "    @RequestMapping(\"ADDR\")\n" +
            "    public Map template(HttpServletRequest request, HttpServletResponse response) {\n" +
            "        return WebSocketUtil.deal(request, response);\n" +
            "    }\n" +
            "\n" +
            "}";


    static final String JAVA_SOURCE_CODE_STRING = "package com.yonyougov.http.registered;\n" +
            "\n" +
            "import com.yonyougov.http.util.WebSocketUtil;\n" +
            "import org.slf4j.Logger;\n" +
            "import org.slf4j.LoggerFactory;\n" +
            "import org.springframework.web.bind.annotation.RequestMapping;\n" +
            "import org.springframework.web.bind.annotation.RestController;\n" +
            "\n" +
            "import javax.servlet.http.HttpServletRequest;\n" +
            "import javax.servlet.http.HttpServletResponse;\n" +
            "\n" +
            "@RestController\n" +
            "public class SERVICE_NAMEStringTemplateController {\n" +
            "    private static Logger log = LoggerFactory.getLogger(SERVICE_NAMEStringTemplateController.class);\n" +
            "\n" +
            "    @RequestMapping(\"ADDR\")\n" +
            "    public String template(HttpServletRequest request, HttpServletResponse response) {\n" +
            "        return WebSocketUtil.dealString(request, response);\n" +
            "    }\n" +
            "\n" +
            "}";

    static Pattern p = Pattern.compile("\\s*|\t|\r|\n");

    public static void regis(String serviceName, InterfaceNode node, Integer type) {
        try {
            Class aClass = null;
            aClass = InterfaceRepo.idClassMaps.get(node.getId());
            if (ObjectUtils.isEmpty(aClass)) {
                String replace = "";
                if (node.getResultType().equals("json")) {
                    replace = StringUtils.replace(JAVA_SOURCE_CODE_JSON, "SERVICE_NAME", serviceName);
                } else {
                    replace = StringUtils.replace(JAVA_SOURCE_CODE_STRING, "SERVICE_NAME", serviceName);
                }

                replace = StringUtils.replace(replace, "ADDR", node.getAddr());
                if (ObjectUtils.isEmpty(node.getResult())) {
                    return;
                }

                GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
                aClass = groovyClassLoader.parseClass(replace);

                InterfaceRepo.idClassMaps.put(node.getId(), aClass);
            }
            ApplicationContext applicationContext = SpringBeanUtils.getApplicationContext();
            MappingRegulator.controlCenter(aClass, applicationContext, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void regis(InterfaceNode node) {
        int i = InterfaceRepo.count.addAndGet(1);
        String serviceName = "Reigs" + i;
        regis(serviceName, node, 1);
    }

    public static void unregis(InterfaceNode node) {
        regis(null, node, 3);
    }

}
