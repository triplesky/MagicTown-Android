package com.bekids.gogotown.unity.bridge.result;

/**
 * Author: LuckyFind
 * Date: 2021/3/5
 * Desc:
 */
public class FloatResult {
    private String type;
    private float value;

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public FloatResult(float value) {
        this.type = "num";
        this.value = value;
    }

    public FloatResult() {
        this.type = "num";
    }
}
