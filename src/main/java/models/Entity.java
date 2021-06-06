package models;

public class Entity {
    private final EntityID entityId;
    private final String name;
    private final String ipAddrress;

    public Entity(EntityID entityId, String name, String ipAddrress){
        this.entityId = entityId;
        this.name = name;
        this.ipAddrress = ipAddrress;
    }
}
