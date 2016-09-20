package com.easemob.weichat.integration.rest.service;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.easemob.weichat.integration.data.NewVersionInfo;
import com.easemob.weichat.integration.data.ReceivedId;
import com.easemob.weichat.models.data.ApiResponse;

import lombok.extern.slf4j.Slf4j;
/**
 * @author shengyp
 * @since 09/08/16
 */

@Slf4j
@Component
public class SysInfoService implements ISysInfoService {
    @Autowired
    @Qualifier("newSysInfoRedisTemplate")
    private RedisTemplate<String, String> newVersionRedisTemplate;

    @Value("${kf.newSysInfo.latestVerInfo.key}")
    private String latestVerisonInfo;

    @Value("${kf.newSysInfo.latestVerInfo.id.key}")
    private String idKey;

    @Value("${kf.newSysInfo.latestVerInfo.content.key}")
    private String contentKey;

    @Value("${kf.newSysInfo.tenantSet.key.prefix}")
    private String tenantSetKey;

    @Value("${kf.newSysInfo.agentSet.key.prefix}")
    private String agentSetPrefix;

    @Value("${kf.newSysInfo.agentSet.key.timeout}")
    private long agentSetExpireTime;

    @Override
    public NewVersionInfo doCheckVerInfoRead(Integer tenantId, String agentId)
    {
        NewVersionInfo newVersionInfoData = getVersionInfo();
        if ( newVersionInfoData.getId() == null || newVersionInfoData.getId().length() == 0){
            // 查看hash中的ID是否有值，如果为空，返回false，不需要发布版本信息通知
            newVersionInfoData.setFlag(false);
            return newVersionInfoData;
        }

        addTenantIdIntoSet(tenantId.toString());
        if (checkAgentidExistInSet(tenantId, agentId)) {
            // 如果存在，则不需要向该agent推送
            newVersionInfoData.setFlag(false);
            return newVersionInfoData;
        }
        
        newVersionInfoData.setFlag(true);
        return newVersionInfoData;
    }

    @Override
    public boolean doAgentUserRead(Integer tenantId, String agentId, ReceivedId receivedId) {
        boolean result = false;
        
        NewVersionInfo savedVersionInfoData = getVersionInfo();
        if ( (savedVersionInfoData.getId() == null) || (savedVersionInfoData.getId().length() == 0)){
            // 查看hash中的ID是否有值，如果为空，返回false，不需要发布版本信息通知
            return result;
        }

        if ( !savedVersionInfoData.getId().equals(receivedId.getId()) ){
            // 判断收到前端的ID是否当前发布的ID相同。如果不同，临界异常情况，已发布新版本，但用户一直未刷新界面
            // 则用户还是用的旧版本发布的ID，不操作
            return result;
        }

        addTenantIdIntoSet(tenantId.toString());

        if (!checkAgentidExistInSet(tenantId, agentId)) {
            // 如果该agentId不在SET:agent中，收到该POST请求，往SET:agent中添加该agentId。
            addAgentidIntoSet(tenantId, agentId);
            result = true;
        }

        return result;
    }

    @Override
    public boolean doAddNewVersion(NewVersionInfo newVersionInfoData) {
        NewVersionInfo savedVersionInfoData = getVersionInfo();

        // 检查REDIS中是否有上次保存的hash key：id的值存在
        if ( savedVersionInfoData.getId() != null && savedVersionInfoData.getId().length() != 0){
            // 判断是否与REDIS上次保存的key:id的值相同，如果相同，则跳过本次操作
            if (savedVersionInfoData.getId().equals(newVersionInfoData.getId()))
            {
                return false;
            }
            // 先删除已经保存版本ID的 SET:agentId
            deleteAgentUserFromSet();
            // 从hash中删除旧的ID值和content值
            deleteVersionInfo();
        }

        // 保存newVersionInfoData到hash中
        saveVersionInfo(newVersionInfoData);
        return true;
    }

    @Override
    public NewVersionInfo getVersionInfo(){
        NewVersionInfo newVersionInfoData = new NewVersionInfo();
        newVersionInfoData.setId((String) newVersionRedisTemplate.boundHashOps(latestVerisonInfo).get(idKey));
        newVersionInfoData.setContent((String) newVersionRedisTemplate.boundHashOps(latestVerisonInfo).get(contentKey));

        return newVersionInfoData;
    }

    @Override
    public void saveVersionInfo(NewVersionInfo newVersionInfoData){
        newVersionRedisTemplate.boundHashOps(latestVerisonInfo).put(idKey, newVersionInfoData.getId());
        newVersionRedisTemplate.boundHashOps(latestVerisonInfo).put(contentKey, newVersionInfoData.getContent());
    }

    @Override
    public void deleteVersionInfo(){
        newVersionRedisTemplate.boundHashOps(latestVerisonInfo).delete(idKey);
        newVersionRedisTemplate.boundHashOps(latestVerisonInfo).delete(contentKey);
    }

    @Override
    public void deleteAgentUserFromSet(){
        // 删除已经存储的SET:agent
        String savedId = (String) newVersionRedisTemplate.boundHashOps(latestVerisonInfo).get(idKey);
        log.info("hashname {}  idkey {}  contentKey{}",latestVerisonInfo, idKey, contentKey);
        for (String tenantId : newVersionRedisTemplate.boundSetOps(tenantSetKey).members()){
            String agentKey = agentSetPrefix + ":" + tenantId.toString() + ":" + savedId;
            newVersionRedisTemplate.delete(agentKey);
        }
    }

    @Override
    public void addTenantIdIntoSet(String tenantId){
        if (!newVersionRedisTemplate.boundSetOps(tenantSetKey).isMember(tenantId)) {
             // 如何不存在tenantId，则添加
             newVersionRedisTemplate.boundSetOps(tenantSetKey).add(tenantId);
        }
    }

    @Override
    public boolean checkAgentidExistInSet(Integer tenantId, String agentId){
        String savedId = (String) newVersionRedisTemplate.boundHashOps(latestVerisonInfo).get(idKey);
        String agentKey = agentSetPrefix + ":" + tenantId.toString() + ":" + savedId;

        return newVersionRedisTemplate.boundSetOps(agentKey).isMember(agentId);
    }

    @Override
    public void addAgentidIntoSet(Integer tenantId, String agentId){
        String savedId = (String) newVersionRedisTemplate.boundHashOps(latestVerisonInfo).get(idKey);
        String agentKey = agentSetPrefix + ":" + tenantId.toString() + ":" + savedId;

        // 添加agentId到SET:agentId中
        newVersionRedisTemplate.boundSetOps(agentKey).add(agentId);
        // 设置默认超时时间，以防异常情况key不能删除时，自动回收
        newVersionRedisTemplate.boundValueOps(agentKey).expire(agentSetExpireTime, TimeUnit.DAYS);
    }
}
