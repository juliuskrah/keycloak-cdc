package com.juliuskrah.cdc.event;

/**
 * Representation of events emitted by {@link com.juliuskrah.cdc.sink.KafkaConsumer}
 * 
 * @author Julius Krah
 */
public class SseEvent {
    private final String type;
    public static final String USER_ADDED_EVENT = "UserAddedEvent";
    public static final String USER_UPDATED_EVENT = "UserUpdatedEvent";
    public static final String USER_DELETED_EVENT = "UserDeletedEvent";
    public static final String ROLE_ADDED_EVENT = "RoleAddedEvent";
    public static final String ROLE_UPDATED_EVENT = "RoleUpdatedEvent";
    public static final String ROLE_DELETED_EVENT = "RoleDeletedEvent";
    public static final String USER_ADDED_TO_ROLE_EVENT = "UserAddedToRoleEvent";
    public static final String USER_UPDATED_IN_ROLE_EVENT = "UserUpdatedInRoleEvent";
    public static final String USER_REMOVED_FROM_ROLE_EVENT = "UserRemovedFromRoleEvent";

    private Object source;

    public SseEvent(String type) { this.type = type; }

    public String getType() {
        return type;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "Event [source=" + source + ", type=" + type + "]";
    }
    
}