package com.bekids.gogotown.unity.bridge.strategy;//package com.ihuman.enlighten.unity.bridge.strategy;


import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod;
import com.bekids.gogotown.unity.bridge.bean.MessageConstants;
import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy;
import com.ihuman.library.accountsdk.IHNetworkHelp;
import com.ihuman.sdk.lib.utils.NetworkChangeUtils;
import com.ihuman.sdk.lib.utils.net.IHNetworkType;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Author: LuckyFind
 * Date: 2021/3/31
 * Desc:网络状态相关
 * };
 */
@RegisterUnityMethod({MessageConstants.MESSAGE_CURRENT_NETWORK_STATUS, MessageConstants.MESSAGE_NETWORK_ERROR_STATUS})
public class NetworkStrategy extends BaseAbstractStrategy {
    @Override
    protected String process(String method, JSONArray jsonArray, String blockId) throws JSONException, IndexOutOfBoundsException {
        String result = null;
        switch (method) {
            case MessageConstants.MESSAGE_CURRENT_NETWORK_STATUS:
                result = generateNumJson(NetworkChangeUtils.getInstance().currentNetworkStatus());
                break;
            case MessageConstants.MESSAGE_NETWORK_ERROR_STATUS:
                result = getCurrentNetworkErrorStatus(NetworkChangeUtils.getInstance().currentNetworkStatus());
                break;
        }
        return result;
    }


    /**
     * 当前网络无法使用的原因
     *
     * @param currentNetworkStatus
     * @return
     */
    private String getCurrentNetworkErrorStatus(int currentNetworkStatus) {
        int status = 0;
        switch (currentNetworkStatus) {
            case IHNetworkType.IHNetworkStatusUnKnow:
                break;
            case IHNetworkType.IHNetworkStatusNotReachable:
                status = 4;
                break;
            case IHNetworkType.IHNetworkStatusViaWWan:
                status = 1;
                break;
            case IHNetworkType.IHNetworkStatusViaWiFi:
                status = 2;
                break;
            case IHNetworkType.IHNetworkStatusConnected:
                status = 0;
                break;
        }
        return generateNumJson(status);
    }
}
