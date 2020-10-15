package com.ciecc.common.workflow;

public class WorkflowException extends Exception{

    public WorkflowException(){

    }

    public WorkflowException(String msg){
        super(msg);
    }

    public WorkflowException(String msg,Throwable throwable){
        super(msg,throwable);
    }
}
