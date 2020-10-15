package com.ciecc.common.workflow;

import org.springframework.stereotype.Service;

@Service("testService")
public class TestService {

    public String test(String msg){
        System.out.println(msg);
        return msg;
    }
}
