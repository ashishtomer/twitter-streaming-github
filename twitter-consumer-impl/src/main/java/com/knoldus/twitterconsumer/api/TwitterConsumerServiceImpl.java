package com.knoldus.twitterconsumer.api;

import akka.Done;
import akka.NotUsed;
import akka.stream.javadsl.Flow;
import com.google.inject.Inject;
import com.knoldus.twitterproducer.api.TwitterProducerService;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import java.util.concurrent.CompletableFuture;

public class TwitterConsumerServiceImpl implements TwitterConsumerService {

    private final TwitterProducerService twitterProducerServiceApi;

    @Inject
    public TwitterConsumerServiceImpl(TwitterProducerService twitterProducerServiceApi) {
        this.twitterProducerServiceApi = twitterProducerServiceApi;
    }

    @Override
    public ServiceCall<NotUsed, String> consumeTweets() {
        return nothing -> {
            twitterProducerServiceApi.tweetsTopic()
                    .subscribe()
                    .atLeastOnce(Flow.fromFunction( (tweetString) -> {
                        System.out.println(tweetString);
                        return Done.getInstance();
                    }));
            return CompletableFuture.completedFuture("Consuming Tweets");
        };
    }

}
