package com.easemob.weichat.integration.rest.service;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.easemob.weichat.integration.data.NewVersionInfo;
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
