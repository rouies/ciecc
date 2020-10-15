package com.ciecc.common.workflow;

import com.ciecc.common.workflow.runtime.WorkflowRuntimeException;
import com.ciecc.common.workflow.runtime.xml.XmlStateWorkflowInitializer;
import com.ciecc.common.workflow.utils.BeanUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) throws IOException, WorkflowRuntimeException, WorkflowException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.ciecc.common.workflow");
        String path = Thread.currentThread().getContextClassLoader().getResource("state_demo.xml").getPath();
//        XmlStateWorkflowInitializer obj = new XmlStateWorkflowInitializer(path);
//        obj.initialize();
//        System.out.println("over");
        WorkflowEngine.getInstance().registerActivities(path);
        ConcurrentHashMap<String,Object> dataset = new ConcurrentHashMap<>();
        dataset.put("name","lisi");
        String instanceID = WorkflowEngine.getInstance().start("test_demo", dataset);
        while(true){
            int read = System.in.read();
            WorkflowEngine.getInstance().next(instanceID);
        }
    }
}
