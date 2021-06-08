package models;

public class EventID {
    private final String id;

    private EventID(String id){
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
