package com.bekids.gogotown.unity.bridge.result;

/**
 * Author: LuckyFind
 * Date: 2021/3/4
 * Desc:
 */
public class LongResult {
    private String type;
    private long value;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public LongResult(long value) {
        this.type = "num";
        this.value = value;
    }

    public LongResult() {
        this.type = "num";
    }
}
