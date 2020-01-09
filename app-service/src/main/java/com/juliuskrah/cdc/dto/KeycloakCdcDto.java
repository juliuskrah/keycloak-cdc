package com.juliuskrah.cdc.dto;

import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for events emitted from Kafka. The Json payload emitted by Debezium is a bit unwindy. 
 * This class attempts to simplify the structure for use
 * 
 * @author Julius Krah
 */
public class KeycloakCdcDto<T> {
    private Map<String, Object> schema;
    private Payload<T> payload;

    public Map<String, Object> getSchema() {
        return schema;
    }

    public void setSchema(Map<String, Object> schema) {
        this.schema = schema;
    }

    public Payload<T> getPayload() {
        return payload;
    }

    public void setPayload(Payload<T> payload) {
        this.payload = payload;
    }

    public static class Payload<T> {
        private T before;
        private T after;
        private Map<String, Object> source;
        private char op;
        @JsonProperty("ts_ms")
        private Instant tsMs;

        public T getBefore() {
            return before;
        }

        public void setBefore(T before) {
            this.before = before;
        }

        public T getAfter() {
            return after;
        }

        public void setAfter(T after) {
            this.after = after;
        }

        public Map<String, Object> getSource() {
            return source;
        }

        public void setSource(Map<String, Object> source) {
            this.source = source;
        }

        public char getOp() {
            return op;
        }

        public void setOp(char op) {
            this.op = op;
        }

        public Instant getTsMs() {
            return tsMs;
        }

        public void setTsMs(Instant tsMs) {
            this.tsMs = tsMs;
        }
    }
    
}