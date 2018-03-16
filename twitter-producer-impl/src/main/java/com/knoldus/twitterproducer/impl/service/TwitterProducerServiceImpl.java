package com.knoldus.twitterproducer.impl.service;

import akka.NotUsed;
import akka.japi.Pair;
import com.google.inject.Inject;
import com.knoldus.twitterproducer.api.TwitterProducerService;
import com.knoldus.twitterproducer.impl.eventsourcing.command.ProduceTweetCommand;
import com.knoldus.twitterproducer.impl.eventsourcing.command.TwitterProducerCommand;
import com.knoldus.twitterproducer.impl.eventsourcing.entity.TwitterProducerEntity;
import com.knoldus.twitterproducer.impl.eventsourcing.event.TweetProducedEvent;
import com.knoldus.twitterproducer.impl.eventsourcing.event.TwitterProducerEvent;
import com.knoldus.twitterproducer.impl.twitter.TweetProducer;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.broker.TopicProducer;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import scala.concurrent.duration.FiniteDuration;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class TwitterProducerServiceImpl implements TwitterProducerService {

    private final PersistentEntityRegistry persistentEntityRegistry;
    private final TweetProducer producer;

    @Inject
    public TwitterProducerServiceImpl(PersistentEntityRegistry persistentEntityRegistry, TweetProducer producer) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        this.producer = producer;
        persistentEntityRegistry.register(TwitterProducerEntity.class);
    }

    @Override
    public ServiceCall<NotUsed, String> pingService() {
        return request -> {
            return CompletableFuture.completedFuture("I am alive.");
        };
    }

    @Override
    public ServiceCall<NotUsed, String> pingServicePath() {
        return request -> CompletableFuture.completedFuture("Path alive");
    }

    @Override
    public ServiceCall<String, String> handleData() {
        return data -> entityRef(LocalDateTime.now().toString())
            .withAskTimeout(new FiniteDuration(3, TimeUnit.SECONDS))
            .ask(new ProduceTweetCommand(data))
            .thenApply(done -> "The data {{ " + data + " }} has been persisted in the event store.");
    }

    @Override
    public ServiceCall<NotUsed, String> produceTweets() {
        return nothing -> {
            producer.startProducing();
            return CompletableFuture.completedFuture("Going to produce the tweets.");
        };
    }

    private PersistentEntityRef<TwitterProducerCommand> entityRef(String timeNow) {
        return persistentEntityRegistry.refFor(TwitterProducerEntity.class, timeNow);
    }

    @Override
    public Topic<String> tweetsTopic() {
        return TopicProducer.taggedStreamWithOffset(TwitterProducerEvent.TAG.allTags(), (tag, offset) ->
            persistentEntityRegistry.eventStream(tag, offset).map(eventAndOffset -> {
                String eventToPublish;

                if(eventAndOffset.first() instanceof TweetProducedEvent) {
                    String tweetProduced = ((TweetProducedEvent)eventAndOffset.first()).getTweet();
                    eventToPublish = tweetProduced;
                } else {
                    throw new IllegalArgumentException("Unknown event: " + eventAndOffset.first());
                }
                return Pair.create(eventToPublish, eventAndOffset.second());
            })
        );
    }

}
