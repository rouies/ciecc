package com.ciecc.common.workflow;

import com.ciecc.common.workflow.data.IPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;

@Configuration
public class BeanConfiguration {
    @Bean
    @Primary
    public WorkflowRegistrant getWorkflowRegistrant(){
        return new MemoryWorkflowRegistrant();
    }


    @Bean
    @Primary
    public IPersistence getPersistence(){
        return new IPersistence() {
            @Override
            public void save(String workflowID, String instanceId, String currentActId, Map<String, Object> data) throws WorkflowException {

            }
        };
    }
}
