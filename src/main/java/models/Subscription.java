package models;

import java.util.function.Function;

public class Subscription {
    private final Topic topicId;
    private final EntityID subscriptionId;
    private final SubscriptionType type;
    private final Function<Event, Boolean> recondition;
    private final Function<Event, Void> handler;

    public Subscription(final Topic topicId,
                        final EntityID subscriptionId,
                        final SubscriptionType type,
                        final Function<Event, Boolean> recondition,
                        final Function<Event, Void> handler
    ){
        this.topicId = topicId;
        this.subscriptionId = subscriptionId;
        this.type = type;
        this.recondition = recondition;
        this.handler = handler;
    }

    public EntityID getSubscriberId() {
        return subscriptionId;
    }

    public Topic getTopicId() {
        return topicId;
    }

    public Function<Event, Void> handler() {
        return handler;
    }

    public SubscriptionType getType() {
        return type;
    }
}
