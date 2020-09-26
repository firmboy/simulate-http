package com.yonyougov.http.util;

import com.yonyougov.http.entity.InterfaceNode;
import com.yonyougov.http.repo.InterfaceRepo;
import groovy.lang.GroovyClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
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

    static final String JAVA_SOURCE_CODE = "package com.yonyougov.http.registered;\n" +
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
            "public class SERVICE_NAMETemplateController {\n" +
            "    private static Logger log = LoggerFactory.getLogger(SERVICE_NAMETemplateController.class);\n" +
            "\n" +
            "    @RequestMapping(\"/ADDR\")\n" +
            "    public Map template(HttpServletRequest request, HttpServletResponse response) {\n" +
            "        return WebSocketUtil.deal(request, response);\n" +
            "    }\n" +
            "\n" +
            "}\n";

    static Pattern p = Pattern.compile("\\s*|\t|\r|\n");

    public static void regis(String serviceName, String addr, String result) {
        try {
            String replace = StringUtils.replace(JAVA_SOURCE_CODE, "SERVICE_NAME", serviceName);
            replace = StringUtils.replace(replace, "ADDR", addr);
            result = result.replace("\"", "\\\"");
            //去除回车换行
            Matcher m = p.matcher(result);
            result = m.replaceAll("");
            
            replace = StringUtils.replace(replace, "RESULT", result);
            GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
            Class aClass = groovyClassLoader.parseClass(replace);

            ApplicationContext applicationContext = SpringBeanUtils.getApplicationContext();
            MappingRegulator.controlCenter(aClass, applicationContext, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String buildClassPath(String s) {
        return s;
    }


    public static void regis(InterfaceNode node) {
        Map<String, byte[]> results = null;
        int i = InterfaceRepo.count.addAndGet(1);
        String serviceName = "Reigs" + i;
        regis(serviceName, node.getAddr(), node.getResult());
    }

}
