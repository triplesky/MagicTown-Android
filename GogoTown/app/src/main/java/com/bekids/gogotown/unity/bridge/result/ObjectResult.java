package com.bekids.gogotown.unity.bridge.result;

/**
 * Author: LuckyFind
 * Date: 2021/2/3
 * Desc:
 */
public class ObjectResult<T> {
    private String type;
    private T value;

    public ObjectResult() {
        this.type = "obj";
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
