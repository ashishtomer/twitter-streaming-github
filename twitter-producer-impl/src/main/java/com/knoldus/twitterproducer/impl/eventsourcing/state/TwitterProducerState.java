package com.knoldus.twitterproducer.impl.eventsourcing.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.NonNull;
import lombok.Value;

import java.util.Optional;

@Value
@JsonDeserialize
public final class TwitterProducerState implements CompressedJsonable {

    public Optional<String> tweet;
    public String timeOfTweet;

    @JsonCreator
    public TwitterProducerState(@NonNull Optional<String> tweet, @NonNull String timeOfTweet) {
        this.tweet = tweet;
        this.timeOfTweet = timeOfTweet;
    }
}
