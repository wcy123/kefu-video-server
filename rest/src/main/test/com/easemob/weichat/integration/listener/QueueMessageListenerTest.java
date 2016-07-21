package com.easemob.weichat.integration.listener;

import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.jgroups.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.easemob.weichat.integration.IntegrationTestApplication;
import com.easemob.weichat.models.entity.ServiceSession;
import com.easemob.weichat.models.entity.VisitorUser;
import com.easemob.weichat.models.enums.ChannelType;
import com.easemob.weichat.models.message.EasemobMessage;
import com.easemob.weichat.models.util.JSONUtil;
import com.easemob.weichat.service.data.QueueMessage;
import com.easemob.weichat.service.events.AbstractEvent;
import com.easemob.weichat.service.events.QueueMessageEvent;
import com.easemob.weichat.service.events.session.ServiceSessionCreatedEvent;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;




@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IntegrationTestApplication.class)
@WebAppConfiguration
public class QueueMessageListenerTest {

	
	
	@Autowired
	private TestEventListener testEventListener;
	
	@Test
	public void test() {
		
		ServiceSessionCreatedEvent careate = new ServiceSessionCreatedEvent();
		careate.setTenantId(1);
		
		ServiceSession serviceSession = new ServiceSession();
		serviceSession.setServiceSessionId(UUID.randomUUID().toString());
		serviceSession.setTenantId(1);
		serviceSession.setAgentUserId(UUID.randomUUID().toString());
		serviceSession.setVisitorUser(new VisitorUser(UUID.randomUUID().toString(), 1, 1, "18710095403", 1l));
		serviceSession.setTechChannelType(ChannelType.CALLCENTER.getType());
		
		testEventListener.clear();
		
		careate.setServiceSession(serviceSession);
		
		EasemobMessage easemobMessage = new EasemobMessage();
		
		// 构造消息体
        ObjectNode root = JSONUtil.getObjectMapper().createObjectNode();
        ObjectNode weichat = JSONUtil.getObjectMapper().createObjectNode();
        root.set("weichat", weichat);
        
        ArrayNode jsonArray = JSONUtil.getObjectMapper().createArrayNode();
        for (int i = 0; i < 1; i++) {
            ObjectNode temp = JSONUtil.getObjectMapper().createObjectNode();
            temp.put("growingid", UUID.randomUUID().toString() );
            jsonArray.add(temp);
        }
        weichat.set("visitor", jsonArray);
		easemobMessage.setExt(root);
		
		careate.setEasemobMessage(easemobMessage);
		
		
		// 测试是否加入队列之中，最终在redis队列之中，由于是异步调用，所以需要sleep一下
		
	    try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		Queue<AbstractEvent> list = testEventListener.getList();
		Assert.assertTrue(list.size() == 1);
		AbstractEvent poll = list.poll();
		Assert.assertNotNull(poll);
		Assert.assertTrue(poll instanceof QueueMessageEvent);
		QueueMessageEvent messageEvent = (QueueMessageEvent) poll;
		List<QueueMessage> messages = messageEvent.getQueueMessageData().getMessages();
		Assert.assertTrue(messages.size() == 1);
		
		System.console().readLine() ;
		
	}

}
