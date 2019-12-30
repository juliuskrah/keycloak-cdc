package com.juliuskrah.cdc.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User Role Mapping representation from Keycloak
 * 
 * @author Julius Krah
 */
public class UserRoleDto {
    @JsonProperty("user_id")
    private UUID userId;
    @JsonProperty("role_id")
    private UUID roleId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }
    
    @Override
    public String toString() {
        return "UserRoleDto [roleId=" + roleId + ", userId=" + userId + "]";
    }
}