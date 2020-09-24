package com.yonyougov.http.registered;

import com.google.common.collect.Lists;
import com.yonyougov.http.entity.InterfaceNode;
import com.yonyougov.http.entity.Response;
import com.yonyougov.http.repo.InterfaceRepo;
import com.yonyougov.http.repo.InterfaceRepoPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName RegisController
 * @Description TODO
 * @Author playboy
 * @Date 2020/9/22 7:43 下午
 * @Version 1.0
 **/
@RequestMapping
@RestController
public class RegisController {
    private static Logger log = LoggerFactory.getLogger(RegisController.class);

    @Autowired
    RegisService regisService;
    @Autowired
    InterfaceRepoPersistence interfaceRepoPersistence;

    @RequestMapping("/list")
    public List<InterfaceNode> getHttps() {
        return InterfaceRepo.nodes;
    }

    @RequestMapping("/saveInter")
    public Response save(@RequestBody InterfaceNode node) {
        regisService.save(node);
        log.info(node.toString());
        return Response.successReponse(node);
    }

    @RequestMapping("/registered")
    public Response registered(@RequestBody InterfaceNode node) {
        regisService.registered(node);
        log.info(node.toString());
        return Response.successReponse(node);
    }

    @RequestMapping("/delete")
    public Response delete(@RequestBody InterfaceNode node) {
        regisService.delete(node);
        log.info(node.toString());
        return Response.successReponse(node);
    }

    @RequestMapping("/reg")
    public String reg() {
        List<InterfaceNode> nodes = Lists.newArrayList();
        InterfaceNode node = new InterfaceNode();
        node.setAddr("/test/hello");
        nodes.add(node);
        interfaceRepoPersistence.persistence(nodes);
        return "success";
    }


}
