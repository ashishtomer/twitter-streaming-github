package com.knoldus.twitterproducer.impl.service;

import com.google.inject.AbstractModule;
import com.knoldus.twitterproducer.api.TwitterProducerService;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

public class TwitterProducerModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(TwitterProducerService.class, TwitterProducerServiceImpl.class);
    }
}
