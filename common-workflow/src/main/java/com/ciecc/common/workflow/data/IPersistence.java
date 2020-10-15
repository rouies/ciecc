package com.ciecc.common.workflow.data;

import com.ciecc.common.workflow.WorkflowException;

import java.util.Map;

/**
 * 工作流实例持久化操作接口
 */
public interface IPersistence {
    /**
     *
     * @param workflowID
     * @param instanceId
     * @param currentActId
     * @param data
     */
    public void save(String workflowID, String instanceId, String currentActId, Map<String,Object> data) throws WorkflowException;

}
