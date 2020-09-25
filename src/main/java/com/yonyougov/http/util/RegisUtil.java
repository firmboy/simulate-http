package com.yonyougov.http.util;

import com.itranswarp.compiler.JavaStringCompiler;
import com.yonyougov.http.entity.InterfaceNode;
import com.yonyougov.http.registered.RegisService;
import com.yonyougov.http.repo.InterfaceRepo;
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
            JavaStringCompiler compiler = new JavaStringCompiler();
            Map<String, byte[]> results = null;
            String replace = StringUtils.replace(JAVA_SOURCE_CODE, "SERVICE_NAME", serviceName);
            replace = StringUtils.replace(replace, "ADDR", addr);
            result = result.replace("\"", "\\\"");

            //去除回车换行
            Matcher m = p.matcher(result);
            result = m.replaceAll("");

            ClassLoader classLoader1 = RegisUtil.class.getClassLoader();
            log.info("classLoader1类加载器：" + classLoader1.toString());
            ApplicationContext applicationContext = SpringBeanUtils.getApplicationContext();
            RegisService bean = applicationContext.getBean(RegisService.class);
            ClassLoader classLoader = bean.getClass().getClassLoader();
            log.info("bean类加载器：" + classLoader.toString());

            replace = StringUtils.replace(replace, "RESULT", result);
            //log.info(replace);

            Class<?> clazz = null;

//            SpringBootMemoryClassLoader instrance = SpringBootMemoryClassLoader.getInstrance();
//            instrance.registerJava(serviceName + "TemplateController.java", replace);
//            clazz = instrance.findClass(serviceName + "TemplateController.java");

            results = compiler.compile(serviceName + "TemplateController.java", replace);
            byte[] buf = results.get(serviceName + "TemplateController.java");
            clazz = compiler.loadClass("com.yonyougov.http.registered." + serviceName + "TemplateController", results);
            //URLClassLoader classLoader = (URLClassLoader) RegisUtil.class.getClassLoader();
            //classLoader.defineClass(serviceName + "TemplateController.java", buf, 0, buf.length);
            MappingRegulator.controlCenter(clazz, applicationContext, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void regis(InterfaceNode node) {
        Map<String, byte[]> results = null;
        int i = InterfaceRepo.count.addAndGet(1);
        String serviceName = "Reigs" + i;
        regis(serviceName, node.getAddr(), node.getResult());
    }

}
