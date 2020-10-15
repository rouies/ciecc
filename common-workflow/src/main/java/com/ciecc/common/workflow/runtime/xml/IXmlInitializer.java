package com.ciecc.common.workflow.runtime.xml;

import com.ciecc.common.workflow.activities.Activity;
import com.ciecc.common.workflow.activities.sequence.SQActivity;
import com.ciecc.common.workflow.runtime.WorkflowRuntimeException;
import org.dom4j.Element;

public interface IXmlInitializer<T extends SQActivity> {
	public void init(T activity,Element item) throws WorkflowRuntimeException;
}
