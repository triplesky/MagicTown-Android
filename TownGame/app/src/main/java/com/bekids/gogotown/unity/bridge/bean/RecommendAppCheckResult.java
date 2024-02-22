package com.bekids.gogotown.unity.bridge.bean;

import java.util.List;

/**
 * **************************************
 * 项目名称: bekids Town
 *
 * @Author ll
 * 创建时间: 2022/11/23    2:29 下午
 * 用途
 * **************************************
 */
public class RecommendAppCheckResult {
    private boolean Is_ready;

    public boolean isIs_ready() {
        return Is_ready;
    }

    public void setIs_ready(boolean is_ready) {
        Is_ready = is_ready;
    }

    public List<UnityRecommendApp> getApps() {
        return Apps;
    }

    public void setApps(List<UnityRecommendApp> apps) {
        Apps = apps;
    }

    private List<UnityRecommendApp> Apps;
    public static class UnityRecommendApp{
        private String App_id;
        private String Icon_path;

        public String getApp_id() {
            return App_id;
        }

        public void setApp_id(String app_id) {
            App_id = app_id;
        }

        public String getIcon_path() {
            return Icon_path;
        }

        public void setIcon_path(String icon_path) {
            Icon_path = icon_path;
        }

        public boolean isInstalled() {
            return Installed;
        }

        public void setInstalled(boolean installed) {
            Installed = installed;
        }

        private boolean Installed;
    }
}
