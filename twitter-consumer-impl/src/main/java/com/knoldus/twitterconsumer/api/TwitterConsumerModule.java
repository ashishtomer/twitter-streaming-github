package com.knoldus.twitterconsumer.api;

import com.google.inject.AbstractModule;
import com.knoldus.twitterproducer.api.TwitterProducerService;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

public class TwitterConsumerModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(TwitterConsumerService.class, TwitterConsumerServiceImpl.class);
        bindClient(TwitterProducerService.class);
    }
}
