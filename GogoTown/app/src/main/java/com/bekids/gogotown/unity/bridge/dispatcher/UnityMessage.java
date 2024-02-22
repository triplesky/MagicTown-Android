package com.bekids.gogotown.unity.bridge.dispatcher;

/**
 * Author: LuckyFind
 * Date: 2021/1/27
 * Desc:unity传递过来的参数
 */
public class UnityMessage {
    private String methodName;//unity请求的方法名
    private String params;//方法参数列表
    private String blockid;

    public UnityMessage( String methodName, String params, String blockid) {
        this.methodName = methodName;
        this.params = params;
        this.blockid = blockid;
    }


    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getBlockid() {
        return blockid;
    }

    public void setBlockid(String blockid) {
        this.blockid = blockid;
    }
}
