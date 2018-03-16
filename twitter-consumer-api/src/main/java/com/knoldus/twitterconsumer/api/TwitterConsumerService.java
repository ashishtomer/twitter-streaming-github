package com.knoldus.twitterconsumer.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;

import static com.lightbend.lagom.javadsl.api.Service.*;

public interface TwitterConsumerService extends Service {

    ServiceCall<NotUsed, String> consumeTweets();

    @Override
    default Descriptor descriptor() {
        return named("twitter_consumer")
                .withCalls(
                        restCall(Method.GET, "/startConsuming", this::consumeTweets)
                )
//                .withTopics()
                .withAutoAcl(true);
    }
}
