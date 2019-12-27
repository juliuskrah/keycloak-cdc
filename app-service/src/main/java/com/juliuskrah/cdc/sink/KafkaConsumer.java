package com.juliuskrah.cdc.sink;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juliuskrah.cdc.dto.KeycloakCdcDto;
import com.juliuskrah.cdc.dto.UserDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;

/**
 * The various kafka consumers are in this class
 */
@KafkaListener(groupId = "${app.group.id:group-id}", offsetReset = OffsetReset.EARLIEST)
public class KafkaConsumer {
    private final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);
    private final ObjectMapper mapper;

    public KafkaConsumer(ObjectMapper mapper) { this.mapper = mapper; }

    // Debezium writes change events to <server-name>.<schema-name>.<table-name> topic on kafka
    @Topic("keycloak.sample.user_entity")
    void onReceiveUser(@KafkaKey String key, @Nullable String event) {
        log.debug("{}", event);
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
                            }
                            break;
                        case 'u': // Update event capture
                            // For the purpose of testing, we're only interested in 'sample' realm
                            if (payload.getAfter().getRealm().equalsIgnoreCase("sample")) {
                                var user = payload.getAfter();
                                log.info("User updated: {}", user);
                            }
                            break;
                        case 'd': // Delete event capture
                            // We fetch the previous state for delete events
                            // The Replica Identity is set to DEFAULT which means the Primary key
                            // will be available for the previous event
                            var user = payload.getBefore();
                            log.info("User deleted: {}", user);
                            break;
                        default:
                            log.warn("Unknown operation '{}'", payload.getOp());
                            break;
                    }
                }

                return source;
            }).ifPresent(source -> log.debug("Successfully processed {}", source));
    }

    // Debezium writes change events to <server-name>.<schema-name>.<table-name> topic on kafka
    @Topic("keycloak.sample.keycloak_role")
    void onReceiveRole(@KafkaKey String key, @Nullable String event) {
        log.debug("{}", event);
        // TODO 
    }

    // Debezium writes change events to <server-name>.<schema-name>.<table-name> topic on kafka
    @Topic("keycloak.sample.user_role_mapping")
    void onReceiveUserInRole(@KafkaKey String key, @Nullable String payload) {
        log.debug("{}", payload);
        // TODO
    }

    // Utility method to convert strings to generic types
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