package com.ciecc.common.workflow.runtime;

import com.ciecc.common.reflect.ReflectException;

public class WorkflowRuntimeException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public WorkflowRuntimeException(String 反射创建实例失败, Exception e) {

	}

	public WorkflowRuntimeException(String msg) {
		super(msg);
	}

}
