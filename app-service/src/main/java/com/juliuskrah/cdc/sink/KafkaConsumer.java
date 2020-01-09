package com.juliuskrah.cdc.sink;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juliuskrah.cdc.dto.KeycloakCdcDto;
import com.juliuskrah.cdc.dto.RoleDto;
import com.juliuskrah.cdc.dto.UserDto;
import com.juliuskrah.cdc.dto.UserRoleDto;
import com.juliuskrah.cdc.event.SseEvent;
import com.juliuskrah.cdc.repository.ApplicationRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.context.event.ApplicationEventPublisher;

/**
 * The various kafka consumers are in this class
 * 
 * @author Julius Krah
 */
@KafkaListener(groupId = "${app.group.id:group-id}", offsetReset = OffsetReset.EARLIEST)
public class KafkaConsumer {
    private final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);
    private final ObjectMapper mapper;
    private final ApplicationRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    public KafkaConsumer(ObjectMapper mapper,
        ApplicationRepository repository,
        ApplicationEventPublisher eventPublisher) { 
        this.mapper = mapper; 
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Processes real-time events from the users table
     * Debezium writes change events to <server-name>.<schema-name>.<table-name> topic on kafka
     */    
     @Topic("keycloak.sample.user_entity")
    void onReceiveUser(@KafkaKey String key, @Nullable String event) {
        Optional.ofNullable(event)
            .map(source -> {
                var keycloakCdc = unmarshall(source, UserDto.class);
                var payload = keycloakCdc.getPayload();
                if (Objects.nonNull(payload)) {
                    switch (payload.getOp()) {
                        case 'c': // Create event capture
                            // For the purpose of testing, we're only interested in 'sample' realm
                            if (payload.getAfter().getRealm().equalsIgnoreCase("sample")) {
                                var user = payload.getAfter();
                                log.info("User created: {}", user);
                                repository.saveUser(user).subscribe();
                                var sseEvent = new SseEvent(SseEvent.USER_ADDED_EVENT);
                                sseEvent.setSource(user);
                                eventPublisher.publishEvent(sseEvent);
                            }
                            break;
                        case 'u': // Update event capture
                            // For the purpose of testing, we're only interested in 'sample' realm
                            if (payload.getAfter().getRealm().equalsIgnoreCase("sample")) {
                                var user = payload.getAfter();
                                log.info("User updated: {}", user);
                                repository.updateUser(user).subscribe();
                                var sseEvent = new SseEvent(SseEvent.USER_UPDATED_EVENT);
                                sseEvent.setSource(user);
                                eventPublisher.publishEvent(sseEvent);
                            }
                            break;
                        case 'd': // Delete event capture
                            // We fetch the previous state for delete events
                            // The Replica Identity is set to DEFAULT which means the Primary key
                            // will be available for the previous event
                            var user = payload.getBefore();
                            log.info("User deleted: {}", user);
                            repository.deleteUser(user.getId());
                            var sseEvent = new SseEvent(SseEvent.USER_DELETED_EVENT);
                            sseEvent.setSource(user);
                            eventPublisher.publishEvent(sseEvent);
                            break;
                        default:
                            log.warn("Unknown user operation '{}'", payload.getOp());
                            break;
                    }
                }

                return source;
            }).ifPresent(source -> log.debug("Successfully processed User event {}", source));
    }

    /**
     * Processes real-time events from the roles table
     * Debezium writes change events to <server-name>.<schema-name>.<table-name> topic on kafka 
     */
    @Topic("keycloak.sample.keycloak_role")
    void onReceiveRole(@KafkaKey String key, @Nullable String event) {
        Optional.ofNullable(event)
            .map(source -> {
                var keycloakCdc = unmarshall(source, RoleDto.class);
                var payload = keycloakCdc.getPayload();
                if (Objects.nonNull(payload)) {
                    switch (payload.getOp()) {
                        case 'c': // Create event capture
                            // For the purpose of testing, we're only interested in 'sample' realm
                            if (payload.getAfter().getRealm().equalsIgnoreCase("sample")) {
                                var role = payload.getAfter();
                                log.info("Role created: {}", role);
                                repository.saveRole(role).subscribe();
                                var sseEvent = new SseEvent(SseEvent.ROLE_ADDED_EVENT);
                                sseEvent.setSource(role);
                                eventPublisher.publishEvent(sseEvent);
                            }
                            break;
                        case 'u': // Update event capture
                            // For the purpose of testing, we're only interested in 'sample' realm
                            if (payload.getAfter().getRealm().equalsIgnoreCase("sample")) {
                                var role = payload.getAfter();
                                log.info("Role updated: {}", role);
                                repository.updateRole(role).subscribe();
                                var sseEvent = new SseEvent(SseEvent.ROLE_UPDATED_EVENT);
                                sseEvent.setSource(role);
                                eventPublisher.publishEvent(sseEvent);
                            }
                            break;
                        case 'd': // Delete event capture
                            // We fetch the previous state for delete events
                            // The Replica Identity is set to DEFAULT which means the Primary key
                            // will be available for the previous event
                            var role = payload.getBefore();
                            log.info("Role deleted: {}", role);
                            repository.deleteRole(role.getId());
                            var sseEvent = new SseEvent(SseEvent.ROLE_DELETED_EVENT);
                            sseEvent.setSource(role);
                            eventPublisher.publishEvent(sseEvent);
                            break;
                        default:
                            log.warn("Unknown role operation '{}'", payload.getOp());
                            break;
                    }
                }
                return source;
            }).ifPresent(source -> log.debug("Successfully processed Role event {}", source));
    }

    /**
     * Processes real-time events from the user_role table
     * Debezium writes change events to <server-name>.<schema-name>.<table-name> topic on kafka
     */
    @Topic("keycloak.sample.user_role_mapping")
    void onReceiveUserInRole(@KafkaKey String key, @Nullable String event) {
        Optional.ofNullable(event)
            .map(source -> {
                var keycloakCdc = unmarshall(source, UserRoleDto.class);
                var payload = keycloakCdc.getPayload();
                if (Objects.nonNull(payload)) {
                    switch (payload.getOp()) {
                        case 'c': // Create event capture
                            var userRole = payload.getAfter();
                            log.info("User added to role: {}", userRole);
                            repository.saveUserInRole(userRole).subscribe();
                            var sseEvent = new SseEvent(SseEvent.USER_ADDED_TO_ROLE_EVENT);
                            sseEvent.setSource(userRole);
                            eventPublisher.publishEvent(sseEvent);
                            break;
                        case 'u': // Update event capture
                            userRole = payload.getAfter();
                            log.info("User role updated: {}", userRole);
                            repository.updateUserInRole(userRole).subscribe();
                            sseEvent = new SseEvent(SseEvent.USER_UPDATED_IN_ROLE_EVENT);
                            sseEvent.setSource(userRole);
                            eventPublisher.publishEvent(sseEvent);
                            break;
                        case 'd': // Delete event capture
                            // We fetch the previous state for delete events
                            // The Replica Identity is set to DEFAULT which means the Primary key
                            // will be available for the previous event
                            userRole = payload.getBefore();
                            log.info("User with role deleted: {}", userRole);
                            repository.deleteUserFromRole(userRole.getUserId(), userRole.getRoleId());
                            sseEvent = new SseEvent(SseEvent.USER_REMOVED_FROM_ROLE_EVENT);
                            sseEvent.setSource(userRole);
                            eventPublisher.publishEvent(sseEvent);
                            break;
                        default:
                            log.warn("Unknown user-in-role operation '{}'", payload.getOp());
                            break;
                    }
                }
                return source;
            }).ifPresent(source -> log.debug("Successfully processed UserInRole event {}", source));
    }

    /** 
     * Utility method to convert strings to generic types
     */
    private <T> KeycloakCdcDto<T> unmarshall(String json, Class<T> parameterizedType) {
        JavaType type = mapper.getTypeFactory().constructParametricType(KeycloakCdcDto.class, parameterizedType);
        try {
            return mapper.readValue(json, type);
        } catch (IOException ex) {
            throw new RuntimeException("Cannot deserialize '" + KeycloakCdcDto.class.getName()
                    + "' from '" + json + "'", ex);
        }
    }

}