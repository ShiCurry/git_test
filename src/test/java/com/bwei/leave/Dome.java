package com.bwei.leave;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;

public class Dome {

	private RepositoryService repositoryService;
	
	private RuntimeService runtimeService;

	private TaskService taskService;

	private HistoryService historyService;
	
	@Before
	public void init() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		
		repositoryService = processEngine.getRepositoryService();
		
		runtimeService = processEngine.getRuntimeService();
		
		taskService = processEngine.getTaskService();
		
		historyService = processEngine.getHistoryService();
	}
	
	//部署流程图
	@Test
	public void deploy() {
		DeploymentBuilder deployment = repositoryService.createDeployment();
		//部署流程名
		deployment.name("学生请假流程测试");
		//部署流程文件
		deployment.addClasspathResource("leave.bpmn");
		
		Deployment deploy = deployment.deploy();
		System.out.println("发布流程的id=="+deploy.getId());//150001
	}
	
	//创建流程实例
	@Test
	public void createProcessInstance() {
		//开启理财实例
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("leave");
		System.out.println("流程实例的id=="+pi.getId());//12501
	}
	//查询流程
	@Test
	public void queryTask() {
		List<Task> list = taskService.createTaskQuery().processInstanceId("12501").list();
		for (Task task : list) {
			System.out.println("id = "+task.getId()+"-");
			System.out.println("name = "+task.getName()+"-");
			System.out.println("createTime = "+task.getCreateTime()+"-");
			System.out.println("assignee = "+task.getAssignee()+"-");
			System.out.println("-----------------------");
		}
	}
		
	//班长审核
	@Test
	public void commitTask() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("date", 3);
		taskService.complete("12505",map);
	}
		
	//其他审核
	@Test
	public void comTask() {
		taskService.complete("15004");
	}
}
