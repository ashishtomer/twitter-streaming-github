package com.knoldus.twitterproducer.impl.eventsourcing.event;

import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventShards;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;

public interface TwitterProducerEvent extends Jsonable, AggregateEvent<TwitterProducerEvent> {
    AggregateEventShards<TwitterProducerEvent> TAG = AggregateEventTag.sharded(TwitterProducerEvent.class, 4);

    @Override
    default AggregateEventTagger<TwitterProducerEvent> aggregateTag() {
        return TAG;
    }
}
