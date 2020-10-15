package com.ciecc.common.workflow.runtime.xml;

import com.ciecc.common.reflect.ClassReflectUtils;
import com.ciecc.common.reflect.ReflectException;
import com.ciecc.common.text.StringUtils;
import com.ciecc.common.workflow.activities.sequence.SQActivity;
import com.ciecc.common.workflow.activities.state.ActivityType;
import com.ciecc.common.workflow.activities.state.STActivity;
import com.ciecc.common.workflow.utils.STItem;
import com.ciecc.common.workflow.runtime.StateWorkflowInitializer;
import com.ciecc.common.workflow.runtime.WorkflowRuntimeException;
import com.ciecc.common.workflow.utils.XmlUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlStateWorkflowInitializer implements StateWorkflowInitializer {

    private XmlUtils actXml;

    private String[] paths;


    public XmlStateWorkflowInitializer(String...paths){
        this.paths = paths;
        Map<String,String> namespaces = new HashMap<String,String>();
        namespaces.put("wfi","http://louis.com/workflow-activity-config");
        URL resource = Thread.currentThread().getContextClassLoader().getResource("default-workflow-activity-config.xml");
        try {
            this.actXml = new XmlUtils(resource,namespaces);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归创建
     * @param activity
     * @param element
     * @param checkObj
     * @throws WorkflowRuntimeException
     */
    private void createStateNode(STActivity activity,Element element,HashMap<String,STActivity> checkObj) throws WorkflowRuntimeException{
        //设置节点ID
        activity.setId(element.attributeValue("id"));
        checkObj.put(activity.getId(),activity);
        //设置类型
        String type = element.attributeValue("type");
        if("begin".equals(type)){
            activity.setType(ActivityType.BEGIN);
        } else if("end".equals(type)){
            activity.setType(ActivityType.END);
        } else {
            activity.setType(ActivityType.PROCESS);
        }
        //设置初始化节点
        List initList = element.elements("initialize");
        if(initList.size() != 0){
            Element ele1 = (Element)initList.get(0);
            List ele2 = ele1.elements();
            if(ele2.size() != 0){
                Element ele3 = (Element)ele2.get(0);
                //构建节点
                Element activityElement = (Element) this.actXml.getDocument().selectSingleNode(String.format("/wfi:configuration/wfi:activity[@name='%s']", ele3.getName()));
                //实例节点
                try {
                    SQActivity actNode = (SQActivity) ClassReflectUtils.getInstance(activityElement.attributeValue("class"));
                    IXmlInitializer initializer = (IXmlInitializer) ClassReflectUtils.getInstance(activityElement.attributeValue("initializer"));
                    initializer.init(actNode,ele3);
                    activity.setInitialize(actNode);
                } catch (ReflectException e) {
                    throw new WorkflowRuntimeException("反射创建实例失败",e);
                }
            }
        }
        if(activity.getType() != ActivityType.END){
            //设置目标节点
            List targets = element.elements("target");
            for(Object target : targets){
                Element targetEle = (Element) target;
                //目标ID
                String targetId = targetEle.attributeValue("to");
                //目标条件表达式
                Object data = targetEle.getData();
                String exp = StringUtils.isEmptyOrNull(data) ? "1==1" : data.toString();
                STActivity actNode = null;
                if(checkObj.containsKey(targetId)){
                    actNode = checkObj.get(targetId);
                } else {
                    actNode = new STActivity();
                    Element te = (Element) element.selectSingleNode(String.format("/wfi:workflow/wfi:state[@id='%s']", targetId));
                    this.createStateNode(actNode,te,checkObj);
                }
                activity.appendTarget(exp,actNode);
            }
        }
    }

    @Override
    public Map<String, STItem> initialize() throws WorkflowRuntimeException {
        Map<String, STItem> result = new HashMap<String, STItem>();
        Map<String,String> namespaces = new HashMap<String,String>();
        namespaces.put("wfi","http://louis.com/workflow");
        for(String path : this.paths){
            XmlUtils workflowXml = null;
            try {
                workflowXml = new XmlUtils(new File(path).toURI().toURL(),namespaces);
            } catch (DocumentException e) {
                e.printStackTrace();
                continue;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                continue;
            }
            Document document = workflowXml.getDocument();
            //获取流程ID
            String workflowId = document.getRootElement().attributeValue("id");
            //获取开始节点
            Element element = (Element) document.selectSingleNode("/wfi:workflow/wfi:state[@type='begin']");
            HashMap<String,STActivity> checkObj = new HashMap<String,STActivity>();
            STActivity root = new STActivity();
            this.createStateNode(root,element,checkObj);
            STItem stItem = new STItem();
            stItem.setActivities(checkObj);
            stItem.setBeginActivity(root);
            result.put(workflowId,stItem);
        }
        return result;
    }
}
