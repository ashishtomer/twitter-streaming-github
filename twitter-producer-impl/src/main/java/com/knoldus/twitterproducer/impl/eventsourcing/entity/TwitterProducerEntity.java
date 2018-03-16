package com.knoldus.twitterproducer.impl.eventsourcing.entity;

import akka.Done;
import com.knoldus.twitterproducer.impl.eventsourcing.command.ProduceTweetCommand;
import com.knoldus.twitterproducer.impl.eventsourcing.command.TwitterProducerCommand;
import com.knoldus.twitterproducer.impl.eventsourcing.event.TweetProducedEvent;
import com.knoldus.twitterproducer.impl.eventsourcing.event.TwitterProducerEvent;
import com.knoldus.twitterproducer.impl.eventsourcing.state.TwitterProducerState;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.time.LocalDateTime;
import java.util.Optional;

public class TwitterProducerEntity extends PersistentEntity<TwitterProducerCommand, TwitterProducerEvent, TwitterProducerState> {
    @Override
    public Behavior initialBehavior(Optional<TwitterProducerState> snapshotState) {
        BehaviorBuilder b = newBehaviorBuilder(
            snapshotState.orElse(new TwitterProducerState(Optional.empty(), LocalDateTime.now().toString()))
        );

        b.setCommandHandler(ProduceTweetCommand.class, (cmd, ctx) ->
            ctx.thenPersist(new TweetProducedEvent(cmd.getTweet()), evt -> ctx.reply(Done.getInstance()))
        );

        b.setEventHandler(TweetProducedEvent.class,
                evt -> new TwitterProducerState(Optional.ofNullable(evt.getTweet()), LocalDateTime.now().toString()));

        return b.build();
    }
}
