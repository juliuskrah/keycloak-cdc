package com.juliuskrah.cdc.dto;

import java.util.UUID;

/**
 * User Role Mapping representation from Keycloak
 */
public class UserRoleDto {
    private UUID userId;
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