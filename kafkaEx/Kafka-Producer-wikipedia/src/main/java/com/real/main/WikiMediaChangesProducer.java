package com.real.main;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.StreamException;
import com.launchdarkly.eventsource.background.BackgroundEventHandler;

@Service

public class WikiMediaChangesProducer {
	public WikiMediaChangesProducer(KafkaTemplate<String, String> kafkaTemplate) {
		super();
		this.kafkaTemplate = kafkaTemplate;
	}

	private static final Logger logger = LoggerFactory.getLogger(WikiMediaChangesProducer.class);
	
	private KafkaTemplate<String, String> kafkaTemplate;
	
	public void sendMessage() {
		String topic="wikimedia_recentchange";
		BackgroundEventHandler eventHandler=new WikiChangesHandler(kafkaTemplate, topic);
		String url="https://stream.wikimedia.org/v2/event/recentchange";
		EventSource.Builder builder=new EventSource.Builder(URI.create(url));
	
		EventSource eventSource=builder.build();
	
		try {
			eventSource.start();
			TimeUnit.MINUTES.sleep(10);
		} catch (StreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
