package com.knoldus.twitterproducer.impl.eventsourcing.command;

import akka.Done;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.NonNull;
import lombok.Value;

@Value
@JsonDeserialize
public class ProduceTweetCommand implements TwitterProducerCommand, CompressedJsonable, PersistentEntity.ReplyType<Done> {
    public String tweet;

    @JsonCreator
    public ProduceTweetCommand(@NonNull String tweet) {
        this.tweet = tweet;
    }
}
