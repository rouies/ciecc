package com.ciecc.common.workflow;

import com.ciecc.common.identity.UUIDCreator;
import com.ciecc.common.workflow.activities.state.ActivityType;
import com.ciecc.common.workflow.activities.state.STActivity;
import com.ciecc.common.workflow.data.Payload;
import com.ciecc.common.workflow.utils.BeanUtils;
import com.ciecc.common.workflow.utils.STItem;

import java.util.concurrent.ConcurrentHashMap;

public class WorkflowEngine implements WorkflowRegistrant{

    public static volatile WorkflowEngine instance;

    public static WorkflowEngine getInstance(){
        if(instance == null){
            synchronized(WorkflowEngine.class){
                if(instance == null){
                    instance = new WorkflowEngine();
                }
            }
        }
        return instance;
    }

    private WorkflowEngine(){
        this.registrant = BeanUtils.get(WorkflowRegistrant.class);
    }

    private WorkflowRegistrant registrant;



    /**
     * 注册工作流流程
     *
     * @param objs 参数列表，由实现方决定
     */
    @Override
    public void registerActivities(Object... objs) throws WorkflowException {
        this.registrant.registerActivities(objs);
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
        return this.registrant.getActivity(workflowId,actId);
    }

    /**
     * 获取指定流程下的开始节点
     *
     * @param workflowId 流程ID
     * @return 开始节点实例
     */
    @Override
    public STActivity getBeginActivity(String workflowId) {
        return this.registrant.getBeginActivity(workflowId);
    }

    /**
     * 持久化工作流节点
     *
     * @param payload
     * @throws WorkflowException
     */
    @Override
    public void registerInstance(Payload payload) throws WorkflowException {
        this.registrant.registerInstance(payload);
    }

    /**
     * 移除工作流实例
     *
     * @param payload
     * @throws WorkflowException
     */
    @Override
    public void unregisterInstance(Payload payload) throws WorkflowException {
        this.registrant.unregisterInstance(payload);
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
        return this.registrant.getInstance(instanceId);
    }


    /**
     * 启动一个工作流程
     * @param workflowId 要启动的流程ID
     * @param env 启动环境
     */
    public String start(String workflowId, ConcurrentHashMap<String,Object> env) throws WorkflowException{
        STActivity begin = this.getBeginActivity(workflowId);
        if (begin == null) {
            throw new WorkflowException(String.format("流程[%s]:未注册",workflowId));
        }
        //生成实例ID
        String instanceId = new UUIDCreator().create();
        Payload payload = new Payload(env);
        payload.setInstanceId(instanceId);
        payload.setWorkflowId(workflowId);
        payload.setStateActivity(begin);
        //执行初始化方法
        begin.getInitialize().execute(payload);
        //注册实例
        this.registerInstance(payload);
        return instanceId;
    }

    public void next(String instanceId) throws WorkflowException {
        Payload payload = this.getInstance(instanceId);
        if(payload == null){
            throw new WorkflowException("没有找到对应的实例，该实例可能已经完成");
        }
        STActivity stateActivity = payload.getStateActivity();
        if (stateActivity.getType() == ActivityType.END) {
            System.out.println("已经到最后节点，没有后续流程");
            this.unregisterInstance(payload);
            return;
        }
        Payload next = stateActivity.execute(payload);
        next.save();
        if (next.getStateActivity().getType() == ActivityType.END) {
            System.out.println("已经到最后节点，没有后续流程");
            this.unregisterInstance(payload);
            return;
        }
    }

}
