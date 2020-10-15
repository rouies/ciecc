package com.ciecc.common.workflow.activities.state;

import com.ciecc.common.workflow.activities.Activity;
import com.ciecc.common.workflow.WorkflowException;
import com.ciecc.common.workflow.activities.sequence.SQActivity;
import com.ciecc.common.workflow.data.Payload;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class STActivity extends Activity {

    /**
     * 表达式映射
     */
    private Map<Expression,STActivity> targets = new ConcurrentHashMap<Expression,STActivity>();

    /**
     * 初始化功能节点
     */
    private SQActivity initialize;

    /**
     * 节点类型
     */
    private ActivityType type;

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public SQActivity getInitialize() {
        return initialize;
    }

    public void setInitialize(SQActivity initialize) {
        this.initialize = initialize;
    }

    /**
     * 添加一个目标
     * @param expression 表达式
     * @param activity   目标节点
     */
    public void appendTarget(String expression,STActivity activity){
        Expression compile = AviatorEvaluator.compile(expression);
        targets.put(compile,activity);
    }


    /**
     * 推动节点任务
     * @param payload
     * @return
     * @throws WorkflowException
     */
    @Override
    public Payload execute(Payload payload) throws WorkflowException {
        boolean isExecute = false;
        for (Map.Entry<Expression,STActivity> item : targets.entrySet()) {
            Expression key = item.getKey();
            STActivity next = item.getValue();
            Object expResult = key.execute(payload.getDataSet());
            if (!(expResult  instanceof Boolean)){
                throw new WorkflowException(String.format("表达式错误->[instance:%s][actId:%s]",payload.getInstanceId(),payload.getStateActivity().getId()));
            }
            if((Boolean)expResult){
                //条件成立
                //执行业务方法[@Service]
                if(next.getInitialize() != null){
                    try {
                        payload = next.getInitialize().execute(payload);
                    } catch (WorkflowException e) {
                        //执行失败
                        throw e;
                    }
                }
                payload.setStateActivity(next);
                isExecute = true;
                break;
            }
        }
        if(!isExecute){
            throw new WorkflowException("没有符合条件的目标节点");
        }

        return payload;
    }





}
