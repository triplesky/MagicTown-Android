package com.bekids.gogotown.unity.bridge.interf;

import org.json.JSONObject;

/**
 * Author: LuckyFind
 * Date: 2021/3/8
 * Desc:
 */
public interface IGenerateResultJson {
    /**
     * 生成boolean类型返回值
     * {
     *     type：boolean
     *     value：true or false
     * }
     * */
    String generateBooleanJson(boolean b);

    /**
     * 生成num类型的json，包括float ，int ，long
     * {
     *     type:num
     *     value:number
     * }
     * */
    String generateNumJson(int num);
    String generateNumJson(long num);
    String generateNumJson(float num);

    /**
     * 生成obj类型的json
     * {
     *     type：obj
     *     value:{}
     * }
     *
     * */
    String generateObjJson(JSONObject o);


}
