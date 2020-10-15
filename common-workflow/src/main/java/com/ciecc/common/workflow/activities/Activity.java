package com.ciecc.common.workflow.activities;

import com.ciecc.common.workflow.WorkflowException;
import com.ciecc.common.workflow.data.Payload;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Activity {

    private static final Pattern pattern = Pattern.compile("\\{(.*?)\\}");

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取绑定内容
     * @param text
     * @return
     */
    protected String getBingingValue(String text){
        String result = null;
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    public abstract Payload execute(Payload payload) throws WorkflowException;
}
