package com.easemob.weichat.integration.rest.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.easemob.weichat.integration.data.NewUserTaskData.NewUserTasksResponseData;

import lombok.extern.slf4j.Slf4j;
/**
 * @author shengyp
 * @since 09/28/16
 */

@Slf4j
@Component
public class NewUserTaskService implements INewUserTaskService {
    @Autowired
    @Qualifier("newSysInfoRedisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @Value("${kf.system.newusertask.tasklist.key}")
    private String newUserTaskListKey;
    @Value("${kf.system.newusertask.tasklist.taskname}")
    private String newUserTaskNames;

    @Override
    public boolean doReceiveNewUserRegisterEvent(Integer tenantId, String agentId){
        boolean flag = true;
        String agentTasksKey = String.format("kf:%d:%s:tasklisttodo", tenantId,agentId);

        // 读取系统输入的任务列表，并保存该用户自己的任务列表
        Set<String> taskList = getSystemTaskList();
        if(taskList == null || taskList.isEmpty()){
            log.info("== new user tasks == system tasklist is empty. tenantId={}, agentId={}", tenantId,agentId );
            flag = false;
        }
        else{
            Iterator<String> itr = taskList.iterator();
            while(itr.hasNext()){
                String taskName = itr.next();
                log.info("== new user tasks == adding task：{} into redis for this agent. tenantId={}, agentId={}", taskName, tenantId,agentId);
                redisTemplate.boundSetOps(agentTasksKey).add(taskName);
            }
        }
        return flag;
    }

    @Override
    public NewUserTasksResponseData doGetNewUserTaskList(Integer tenantId, String agentId){
        NewUserTasksResponseData data = new NewUserTasksResponseData();
        ArrayList<String> list = new ArrayList<String>();
        data.setTasklist(list);

        String agentTasksKey = String.format("kf:%d:%s:tasklisttodo", tenantId,agentId);
        if( !redisTemplate.hasKey(agentTasksKey) ){
            data.setNewUserflag(false);
        }
        else{
            for (String taskName : redisTemplate.boundSetOps(agentTasksKey).members()){
                log.info("== new user tasks == tenantId: {}, read task: {}", tenantId, taskName);
                data.getTasklist().add(taskName);
            }
            data.setNewUserflag(true);
        }
        return data;
    }

    @Override
    public boolean doAgentUserTaskDone(Integer tenantId, String agentId, String taskDone){
        String agentTasksKey = String.format("kf:%d:%s:tasklisttodo", tenantId,agentId);
        if (redisTemplate.boundSetOps(agentTasksKey).isMember(taskDone)){
            redisTemplate.boundSetOps(agentTasksKey).remove(taskDone);
            log.info("== new user tasks == tenantId: {}, finish task: {}", tenantId, taskDone);
        }

        return true;
    }

    private Set<String> getSystemTaskList(){
        Set<String> taskSet = new HashSet<String>();
        // 如果redis异常，key值不存在尝试读取配置文件中的任务数目，生成redis数据
        if (!redisTemplate.hasKey(newUserTaskListKey) || redisTemplate.boundSetOps(newUserTaskListKey).size() == 0) {
            String[] strArray = newUserTaskNames.split(",");
            for (int i = 0; i < strArray.length; i++) {
                redisTemplate.boundSetOps(newUserTaskListKey).add(strArray[i]);
                log.info("== new user tasks == generate taskname from configuration file. taskname={}", strArray[i]);
            }
        }

        for (String taskName : redisTemplate.boundSetOps(newUserTaskListKey).members()){
            taskSet.add(taskName);
        }
        return taskSet;
    }
}
