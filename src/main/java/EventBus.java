import models.Subscription;
import models.Timestamp;
import models.*;
import retry.RetryAlgorithm;
import utils.KeyedExecutor;

import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {
    private final KeyedExecutor executor;
    private final Map<Topic, List<Event>> buses;
    private final Map<Topic, Set<Subscription>> subscriptions;
    private final Map<Topic, Map<EntityID, Index>> subscriberIndexes;
    private final Map<Topic, Map<EventID, Index>> eventIndex;
    private final Map<Topic, ConcurrentSkipListMap<Timestamp, Index>> timestampIndex;
    private final RetryAlgorithm<Event, Void> retryAlgorithm;
    private final EventBus deadLetterQueue;
    private final Timer timer;

    private EventBus(final int threads, final RetryAlgorithm<Event, Void> retryAlgorithm,
                     final EventBus deadLetterQueue, final Timer timer){
        this.retryAlgorithm = retryAlgorithm;
        this.deadLetterQueue = deadLetterQueue;
        this.timer = timer;
        this.buses = new ConcurrentHashMap<>();
        this.executor = new KeyedExecutor(threads);
        this.subscriptions = new ConcurrentHashMap<>();
        this.subscriberIndexes = new ConcurrentHashMap<>();
        this.timestampIndex = new ConcurrentHashMap<>();
        this.eventIndex = new ConcurrentHashMap<>();
    }

    public CompletionStage<Void> publish(Topic topic, Event event){
        return executor.submit(topic.getName(), () -> addEventToBus(topic, event));
    }

    public void addEventToBus(Topic topic, Event event) {
        final Index currentIndex = new Index(buses.get(topic).size());
        timestampIndex.get(topic).put(event.getTimestamp(), currentIndex);
        eventIndex.get(topic).put(event.getId(), currentIndex);
        buses.get(topic).add(event);
        subscriptions.getOrDefault(topic, Collections.newSetFromMap(new ConcurrentHashMap<>()))
                .stream()
                .filter(subscription -> SubscriptionType.PUSH.equals(subscription.getType()))
                .forEach(subscription -> push(event, subscription));
    }

    public CompletionStage<Event> poll(Topic topic, EntityID subscriber){
        return executor.get(topic.getName() + subscriber.getId(), () -> {
            final Index index = subscriberIndexes.get(topic).get(subscriber);
            try {
                final Event event = buses.get(topic).put(subscriber, index.increment());
                subscriberIndexes.get(topic).put(subscriber, index.increment());
                return event;
            } catch (IndexOutOfBoundsException exception) {
                return null;
            }
        });
    }

    private void push(Event event, Subscription subscription) {

    }





}
