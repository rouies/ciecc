package com.ciecc.common.workflow;

import com.ciecc.common.workflow.activities.state.STActivity;
import com.ciecc.common.workflow.data.Payload;

/**
 * 工作流注册接口
 */
public interface WorkflowRegistrant {

    /**
     * 注册工作流流程
     * @param objs 参数列表，由实现方决定
     */
    public void registerActivities(Object...objs) throws WorkflowException;

    /**
     * 根据工作流ID和节点ID获取状态实例
     * @param workflowId 工作流ID
     * @param actId 节点ID
     * @return 状态机实例
     */
    public STActivity getActivity(String workflowId, String actId);


    /**
     * 获取指定流程下的开始节点
     * @param workflowId 流程ID
     * @return 开始节点实例
     */
    public STActivity getBeginActivity(String workflowId);

    /**
     * 持久化工作流节点
     * @param payload
     * @throws WorkflowException
     */
    public void registerInstance(Payload payload) throws WorkflowException;


    /**
     * 移除工作流实例
     * @param payload
     * @throws WorkflowException
     */
    public void unregisterInstance(Payload payload) throws WorkflowException;


    /**
     * 获取工作流实例
     * @param instanceId
     * @return
     * @throws WorkflowException
     */
    public Payload getInstance(String instanceId) throws  WorkflowException;
}
