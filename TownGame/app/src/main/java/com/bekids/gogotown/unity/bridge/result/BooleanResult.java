package com.bekids.gogotown.unity.bridge.result;

/**
 * Author: LuckyFind
 * Date: 2021/2/1
 * Desc:
 */
public class BooleanResult {
    private String type;
    private boolean value;

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public BooleanResult(boolean value) {
        this.type = "bool";
        this.value = value;
    }

    public BooleanResult() {
        this.type = "bool";
    }
}
