package com.easemob.weichat.integration.listener;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

import com.easemob.weichat.service.events.AbstractEvent;

public class TestEventListener implements ApplicationListener<AbstractEvent>, Ordered {

	private final Queue<AbstractEvent> list = new ConcurrentLinkedDeque<>();
	
	@Override
    public void onApplicationEvent(AbstractEvent event) {
        list.add(event);
    }

    public void clear() {
        list.clear();
    }

    public Queue<AbstractEvent> getList() {
        return list;
    }

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

}
