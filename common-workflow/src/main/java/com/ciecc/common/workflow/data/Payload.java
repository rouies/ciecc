package com.ciecc.common.workflow.data;

import com.ciecc.common.workflow.WorkflowException;
import com.ciecc.common.workflow.activities.Activity;
import com.ciecc.common.workflow.activities.sequence.SQActivity;
import com.ciecc.common.workflow.activities.state.STActivity;
import com.ciecc.common.workflow.utils.BeanUtils;
import com.googlecode.aviator.AviatorEvaluator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Payload {

    /**
     * 持久化实例
     */
    private IPersistence persistence;

    public Payload(ConcurrentHashMap<String,Object> dataset){
        if(dataset == null){
            this.dataSet = new ConcurrentHashMap<String,Object>();
        } else {
            this.dataSet = dataset;
        }

        this.persistence = BeanUtils.get(IPersistence.class);
    }

    /**
     * 工作流实例ID
     */
    private String instanceId;

    /**
     * 当前流程实例ID
     */
    private String workflowId;

    /**
     * 当前状态流程
     */
    private STActivity stateActivity;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public STActivity getStateActivity() {
        return stateActivity;
    }

    public void setStateActivity(STActivity stateActivity) {
        this.stateActivity = stateActivity;
    }

    /**
     * 当前状态数据集
     */
    private Map<String,Object> dataSet;

    public void setOutput(String key,Object value){
        this.dataSet.put(key,value);
    }

    public Object getInput(String key){
        return this.dataSet.get(key);
    }

    public Map<String,Object> getDataSet(){
        return this.dataSet;
    }

    /**
     * 持久化工作流
     * @throws WorkflowException
     */
    public void save() throws WorkflowException {
        if(this.persistence != null){
            this.persistence.save(this.workflowId,this.instanceId,this.stateActivity.getId(),this.dataSet);
        }
    }



//    public static void main(String[] args) {
//        Map<String,Object> a = new ConcurrentHashMap<String,Object>();
//        a.put("nm","abc");
//        Object execute = AviatorEvaluator.execute("1==1",a);
//        System.out.println(execute.getClass().getName());
//    }

}
