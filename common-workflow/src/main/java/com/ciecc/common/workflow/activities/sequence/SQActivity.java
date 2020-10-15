package com.ciecc.common.workflow.activities.sequence;

import com.ciecc.common.workflow.activities.Activity;
import com.ciecc.common.workflow.WorkflowException;
import com.ciecc.common.workflow.data.Payload;

public abstract class SQActivity extends Activity {

    public abstract void initialize();
}
