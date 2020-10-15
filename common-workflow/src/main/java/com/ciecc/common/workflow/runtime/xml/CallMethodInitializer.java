package com.ciecc.common.workflow.runtime.xml;

import com.ciecc.common.text.StringUtils;
import com.ciecc.common.workflow.activities.sequence.ExternalCallActivity;
import com.ciecc.common.workflow.runtime.WorkflowRuntimeException;
import org.dom4j.Element;

public class CallMethodInitializer implements IXmlInitializer<ExternalCallActivity> {

    @Override
    public void init(ExternalCallActivity activity, Element item) throws WorkflowRuntimeException {
        String bean = item.attributeValue("bean");
        String method = item.attributeValue("method");
        String types = item.attributeValue("types");
        String arguments = item.attributeValue("arguments");
        String result = item.attributeValue("result");
        activity.setBean(bean);
        activity.setMethodName(method);
        activity.setResult(result);
        String[] typesVal = StringUtils.isEmptyOrNull(types) ? null : types.split(",");
        String[] argumentsVal = StringUtils.isEmptyOrNull(arguments) ? null : arguments.split("@");
        activity.setTypes(typesVal);
        activity.setArgumentNames(argumentsVal);
        activity.initialize();

    }
}
