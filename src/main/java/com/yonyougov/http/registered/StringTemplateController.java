package com.yonyougov.http.registered;

import com.yonyougov.http.util.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class StringTemplateController {
    private static Logger log = LoggerFactory.getLogger(StringTemplateController.class);

    @RequestMapping("ADDR")
    public String template(HttpServletRequest request, HttpServletResponse response) {
        return WebSocketUtil.dealString(request, response);
    }

}
