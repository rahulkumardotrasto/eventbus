package models;

import java.util.Map;

public class Event {
    private final EventID id;
    private final String name;
    private final Map<String, String> attributes;
    private final Timestamp timestamp;

    public Event(EventID id, String name, Map<String, String> attributes, Timestamp timestamp){
        this.attributes = attributes;
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
    }

    public EventID getId() {
        return id;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getName() {
        return name;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", attributes=" + attributes +
                ", timestamp=" + timestamp +
                '}';
    }
}
