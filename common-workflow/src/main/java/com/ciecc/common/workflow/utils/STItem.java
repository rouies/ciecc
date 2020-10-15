package com.ciecc.common.workflow.utils;

import com.ciecc.common.workflow.activities.state.STActivity;

import java.util.Map;

public class STItem {

    private STActivity beginActivity;

    private Map<String,STActivity> activities;

    public STActivity getBeginActivity() {
        return beginActivity;
    }

    public void setBeginActivity(STActivity beginActivity) {
        this.beginActivity = beginActivity;
    }

    public Map<String, STActivity> getActivities() {
        return activities;
    }

    public void setActivities(Map<String, STActivity> activities) {
        this.activities = activities;
    }
}
