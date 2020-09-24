package com.yonyougov.http.registered;

import com.yonyougov.http.util.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class TemplateController {
    private static Logger log = LoggerFactory.getLogger(TemplateController.class);

    @RequestMapping("/ADDR")
    public Map template(HttpServletRequest request, HttpServletResponse response) {
        return WebSocketUtil.deal(request, response);
    }

}
