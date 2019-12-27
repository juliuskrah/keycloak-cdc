package com.juliuskrah.cdc.dto;

import java.util.UUID;

/**
 * Representation of Role from Keycloak
 */
public class RoleDto {
    private UUID id;
    private String name;
    private String description;
    private String realm;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    @Override
    public String toString() {
        return "RoleDto [description=" + description + ", id=" + id + ", name=" + name + ", realm=" + realm + "]";
    }
}