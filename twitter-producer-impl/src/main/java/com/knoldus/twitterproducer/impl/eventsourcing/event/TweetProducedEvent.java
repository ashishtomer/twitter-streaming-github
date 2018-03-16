package com.knoldus.twitterproducer.impl.eventsourcing.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.NonNull;
import lombok.Value;

@Value
@JsonDeserialize
public class TweetProducedEvent implements TwitterProducerEvent {
    public String tweet;

    @JsonCreator
    public TweetProducedEvent(@NonNull String tweet) {
        this.tweet = tweet;
    }
}
