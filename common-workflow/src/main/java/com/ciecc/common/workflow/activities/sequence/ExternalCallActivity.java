package com.ciecc.common.workflow.activities.sequence;

import com.ciecc.common.reflect.MemberReflectUtils;
import com.ciecc.common.reflect.ReflectException;
import com.ciecc.common.text.StringUtils;
import com.ciecc.common.workflow.WorkflowException;
import com.ciecc.common.workflow.data.Payload;
import com.ciecc.common.workflow.utils.BeanUtils;
import com.googlecode.aviator.AviatorEvaluator;

public class ExternalCallActivity extends SQActivity{

    private String[] types;

    private String bean;

    private String methodName;

    private String[] argumentNames;

    private String result;

    private MemberReflectUtils<Object> member;

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String[] getArgumentNames() {
        return argumentNames;
    }

    public void setArgumentNames(String[] argumentNames) {
        this.argumentNames = argumentNames;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public Payload execute(Payload payload) throws WorkflowException {
        Object[] args = null;
        if(this.types == null){
            args = new Object[0];
        } else {
            args = new Object[this.types.length];

        }
        for(int i=0,len = args.length;i<len;i++){

            args[i] = AviatorEvaluator.execute(this.argumentNames[i],payload.getDataSet());
        }
        Object result = null;
        try {
            result = this.member.invoke(this.methodName, this.types, args);
        } catch (ReflectException e) {
            throw new WorkflowException("执行方法失败",e);
        }
        if(!StringUtils.isEmptyOrNull(this.result)){
            payload.setOutput(this.result,result);
        }
        return payload;
    }

    @Override
    public void initialize() {
        Object instance = BeanUtils.get(this.bean);
        this.member = new MemberReflectUtils<Object>(instance,false,true);
    }
}
