package com.yonyougov.http.repo;

import com.alibaba.fastjson.JSONObject;
import com.yonyougov.http.entity.InterfaceNode;
import com.yonyougov.http.util.RegisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * @ClassName InterfaceRepoPersistence
 * @Description 接口数据持久化到硬盘
 * @Author playboy
 * @Date 2020/9/24 1:14 下午
 * @Version 1.0
 **/
@Service
public class InterfaceRepoPersistence {
    private static Logger log = LoggerFactory.getLogger(InterfaceRepoPersistence.class);

    static String PATH = "";

    final static String ROOT_PATH = "data";

    final static String INTERFACE_PATH = "interface";

    final static String INTERFACE_FILE_NAME = "interface.json";

    final static String LOG_PATH = "logs";

    static {
        ApplicationHome h = new ApplicationHome(InterfaceRepoPersistence.class.getClass());
        File dir = h.getDir();
        PATH = dir.toString() + File.separator + ROOT_PATH;
    }

    /**
     * 将nodes转为json数据持久化到磁盘
     *
     * @param nodes
     */
    public void persistence(List<InterfaceNode> nodes) {
        String path = PATH + File.separator + INTERFACE_PATH;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filePath = path + File.separator + INTERFACE_FILE_NAME;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            String s = JSONObject.toJSONString(nodes);
            Files.write(Paths.get(filePath), s.getBytes("UTF-8"), StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将磁盘数据持久化到内存
     */
    public void load() {
        log.info("加载磁盘数据到内存");
        String path = PATH + File.separator + INTERFACE_PATH;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filePath = path + File.separator + INTERFACE_FILE_NAME;
        File file = new File(filePath);
        if (file.exists()) {
            //存在磁盘文件
            try {
                byte[] bytes = Files.readAllBytes(Paths.get(filePath));
                String s = new String(bytes, "UTF-8");
                InterfaceRepo.nodes = JSONObject.parseArray(s, InterfaceNode.class);
                //将所有接口注册到
                for (InterfaceNode node : InterfaceRepo.nodes) {
                    loadNode(node);
                }
            } catch (IOException e) {

            }
        }
    }

    private void loadNode(InterfaceNode node) {
        if (node.getIsLeaf()) {
            RegisUtil.regis(node);
            ClassLoader classLoader = InterfaceRepo.class.getClassLoader();
            InterfaceRepo.interMaps.put(node.getAddr(), node);
        }
        InterfaceRepo.idMpas.put(node.getId(), node);
        List<InterfaceNode> children = node.getChildren();
        if (!ObjectUtils.isEmpty(children)) {
            for (InterfaceNode child : children) {
                loadNode(child);
            }
        }
    }

}
