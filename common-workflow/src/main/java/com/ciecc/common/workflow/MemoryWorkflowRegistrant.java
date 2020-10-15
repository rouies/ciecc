package com.ciecc.common.workflow;

import com.ciecc.common.workflow.activities.state.STActivity;
import com.ciecc.common.workflow.data.Payload;
import com.ciecc.common.workflow.runtime.StateWorkflowInitializer;
import com.ciecc.common.workflow.runtime.WorkflowRuntimeException;
import com.ciecc.common.workflow.runtime.xml.XmlStateWorkflowInitializer;
import com.ciecc.common.workflow.utils.STItem;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MemoryWorkflowRegistrant implements WorkflowRegistrant{

    /**
     * 并发控制读写锁
     */
    private ReadWriteLock rwLock = new ReentrantReadWriteLock();

    /**
     * 工作流程信息缓存
     */
    private Map<String, STItem> workflows = new HashMap<String,STItem>();

    /**
     * 工作流实例缓存
     */
    private Map<String,Payload> instances = new ConcurrentHashMap<String,Payload>();



    /**
     * 注册工作流流程
     *
     * @param objs 参数列表，由实现方决定
     */
    @Override
    public void registerActivities(Object... objs) throws WorkflowException {
        rwLock.writeLock().lock();
        String[] paths = new String[objs.length];
        for(int i = 0,len = objs.length;i<len;i++){
            paths[i] = objs[i].toString();
        }
        try {
            StateWorkflowInitializer initializer = new XmlStateWorkflowInitializer(paths);
            Map<String, STItem> res = initializer.initialize();
            for(Map.Entry<String,STItem> entry : res.entrySet()){
                this.workflows.put(entry.getKey(),entry.getValue());
            }
        } catch (WorkflowRuntimeException e) {
            throw new WorkflowException("注册节点失败",e);
        } finally{
            rwLock.writeLock().unlock();
        }
    }

    /**
     * 根据工作流ID和节点ID获取状态实例
     *
     * @param workflowId 工作流ID
     * @param actId      节点ID
     * @return 状态机实例
     */
    @Override
    public STActivity getActivity(String workflowId, String actId) {
        STActivity result = null;
        rwLock.readLock().lock();
        if(this.workflows.containsKey(workflowId)){
            Map<String, STActivity> activities = this.workflows.get(workflowId).getActivities();
            if(activities.containsKey(actId)){
                result = activities.get(actId);
            }
        }
        rwLock.readLock().unlock();
        return result;
    }

    /**
     * 获取指定流程下的开始节点
     *
     * @param workflowId 流程ID
     * @return 开始节点实例
     */
    @Override
    public STActivity getBeginActivity(String workflowId) {
        STActivity result = null;
        rwLock.readLock().lock();
        if(this.workflows.containsKey(workflowId)){
            result = this.workflows.get(workflowId).getBeginActivity();
        }
        rwLock.readLock().unlock();
        return result;
    }

    /**
     * 持久化工作流节点
     *
     * @param payload
     * @throws WorkflowException
     */
    @Override
    public void registerInstance(Payload payload) throws WorkflowException {
        payload.save();
        this.instances.put(payload.getInstanceId(),payload);
    }

    /**
     * 移除工作流实例
     *
     * @param payload
     * @throws WorkflowException
     */
    @Override
    public void unregisterInstance(Payload payload) throws WorkflowException {
        this.instances.remove(payload.getInstanceId());
    }

    /**
     * 获取工作流实例
     *
     * @param instanceId
     * @return
     * @throws WorkflowException
     */
    @Override
    public Payload getInstance(String instanceId) throws WorkflowException {
        return this.instances.get(instanceId);
    }
}
