package com.yonyougov.http.entity;

import java.util.List;

/**
 * @ClassName InterfaceNode
 * @Description 接口实体
 * @Author playboy
 * @Date 2020/9/23 2:44 下午
 * @Version 1.0
 **/
public class InterfaceNode {

    private String id;

    private String text;

    private String addr;

    private String result;

    private String resultType;

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    private List<InterfaceNode> children;

    private boolean isLeaf;

    private String pId;

    private boolean hasRegis;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<InterfaceNode> getChildren() {
        return children;
    }

    public void setChildren(List<InterfaceNode> children) {
        this.children = children;
    }

    public boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public boolean getHasRegis() {
        return hasRegis;
    }

    public void setHasRegis(boolean hasRegis) {
        this.hasRegis = hasRegis;
    }

    @Override
    public String toString() {
        return "InterfaceNode{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", addr='" + addr + '\'' +
                ", result='" + result + '\'' +
                ", children=" + children +
                ", isLeaf=" + isLeaf +
                ", pId='" + pId + '\'' +
                ", hasRegis=" + hasRegis +
                '}';
    }
}
