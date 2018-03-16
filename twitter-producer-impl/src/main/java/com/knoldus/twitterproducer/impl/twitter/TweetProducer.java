package com.knoldus.twitterproducer.impl.twitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.knoldus.twitterproducer.impl.eventsourcing.command.ProduceTweetCommand;
import com.knoldus.twitterproducer.impl.eventsourcing.command.TwitterProducerCommand;
import com.knoldus.twitterproducer.impl.eventsourcing.entity.TwitterProducerEntity;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import lombok.Value;
import twitter4j.*;

import java.time.LocalDateTime;
import java.util.Properties;

@Value
public class TweetProducer {

    private PersistentEntityRegistry persistentEntityRegistry;
    private Properties configProperties = new Properties();
    private ObjectMapper mapper = new ObjectMapper();

    StatusListener listener = new StatusListener() {

        public void onStatus(Status status) {
            try {
                String statusJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(status);
                PersistentEntityRef<TwitterProducerCommand> ref = persistentEntityRegistry.refFor(TwitterProducerEntity.class, LocalDateTime.now().toString());
                ref.ask(new ProduceTweetCommand(statusJson));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        }

        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        }

        public void onScrubGeo(long l, long l1) {
        }

        public void onStallWarning(StallWarning stallWarning) {
        }

        public void onException(Exception ex) {
        }
    };

    @Inject
    public TweetProducer(PersistentEntityRegistry persistentEntityRegistry) {
        this.persistentEntityRegistry = persistentEntityRegistry;
    }

    public void startProducing() {
        TwitterStream twitterStream = new TwitterStreamFactory(TwitterConfiguration.getTwitterConf()).getInstance();
        twitterStream.addListener(listener);
        twitterStream.sample();
    }

}
