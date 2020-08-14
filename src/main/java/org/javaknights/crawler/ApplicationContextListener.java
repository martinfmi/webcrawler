package org.javaknights.crawler;

import java.util.logging.Logger;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextListener implements ApplicationListener<ContextStartedEvent>{

	private static final Logger LOGGER = Logger.getLogger(ApplicationContextListener.class.getName());
	
	@Override
	public void onApplicationEvent(ContextStartedEvent event) {
		LOGGER.info("Application context initialized !!!");
	}

}
