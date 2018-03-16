package com.knoldus.twitterproducer.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import static com.lightbend.lagom.javadsl.api.Service.*;

import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.api.transport.Method;

public interface TwitterProducerService extends Service {

    ServiceCall<NotUsed, String> pingService();
    ServiceCall<NotUsed, String> pingServicePath();
    ServiceCall<String, String> handleData();
    ServiceCall<NotUsed, String> produceTweets();

    Topic<String> tweetsTopic();

    @Override
    default Descriptor descriptor() {
        return named("twitter_producer")
            .withCalls(
                pathCall("/pingPath", this::pingServicePath),
                restCall(Method.GET, "/pingService", this::pingService),
                restCall(Method.POST, "/receiveData", this::handleData),
                restCall(Method.PUT, "/produceTweets", this::produceTweets)
            )
            .withTopics(
                topic("tweets", this::tweetsTopic)
            )
            .withAutoAcl(true);
    }
}
