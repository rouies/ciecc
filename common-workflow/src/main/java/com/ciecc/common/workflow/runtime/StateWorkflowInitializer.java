package com.ciecc.common.workflow.runtime;

import com.ciecc.common.workflow.utils.STItem;

import java.util.Map;

public interface StateWorkflowInitializer {
    public Map<String, STItem> initialize() throws WorkflowRuntimeException;
}
