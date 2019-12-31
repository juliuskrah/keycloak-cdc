package com.juliuskrah.cdc.repository;

import java.time.ZoneOffset;
import java.util.UUID;

import javax.inject.Singleton;

import com.juliuskrah.cdc.dto.RoleDto;
import com.juliuskrah.cdc.dto.UserDto;
import com.juliuskrah.cdc.dto.UserRoleDto;

import io.reactiverse.reactivex.pgclient.PgPool;
import io.reactiverse.reactivex.pgclient.Tuple;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Repository class for managing data access
 * 
 * @author Julius Krah
 */
@Singleton
public class ApplicationRepository {
    private final PgPool client;

    public ApplicationRepository(PgPool client) {
        this.client = client;
    }
    
    public Single<UserDto> saveUser(UserDto user) {
        var tuple = Tuple.tuple();
        tuple.addUUID(user.getId());
        tuple.addString(user.getUsername());
        tuple.addString(user.getEmail());
        tuple.addString(user.getFirstName());
        tuple.addString(user.getLastName());
        tuple.addBoolean(user.isEnabled());
        tuple.addOffsetDateTime(user.getCreatedTimestamp().atOffset(ZoneOffset.UTC));
        return this.client.rxPreparedQuery("INSERT INTO users(id, username, email, "
           + "first_name, last_name, enabled, created_timestamp) "
           + "VALUES($1, $2, $3, $4, $5, $6, $7)", tuple)
           .map(result -> user);
    }

    public Single<Integer> updateUser(UserDto user) {
        var tuple = Tuple.tuple();
        tuple.addUUID(user.getId());
        tuple.addString(user.getUsername());
        tuple.addString(user.getEmail());
        tuple.addString(user.getFirstName());
        tuple.addString(user.getLastName());
        tuple.addBoolean(user.isEnabled());
        tuple.addOffsetDateTime(user.getCreatedTimestamp().atOffset(ZoneOffset.UTC));
        return this.client.rxPreparedQuery("UPDATE users SET username=$2, email=$3, first_name=$4, "
           + "last_name=$5, enabled=$6, created_timestamp=$7 WHERE id=$1", tuple)
           .map(result -> result.rowCount());
    }

    public void deleteUser(UUID userId) {
        this.client.rxPreparedQuery("DELETE FROM users WHERE id=$1", Tuple.of(userId)).subscribe();
    }

    public Single<RoleDto> saveRole(RoleDto role) {
        var tuple = Tuple.tuple();
        tuple.addUUID(role.getId());
        tuple.addString(role.getName());
        tuple.addString(role.getDescription());
        return this.client.rxPreparedQuery("INSERT INTO roles(id, name, description) "
           + "VALUES($1, $2, $3)", tuple)
           .map(result -> role);
    }

    public Single<Integer> updateRole(RoleDto role) {
        var tuple = Tuple.tuple();
        tuple.addUUID(role.getId());
        tuple.addString(role.getName());
        tuple.addString(role.getDescription());
        return this.client.rxPreparedQuery("UPDATE roles SET name=$2, description=$3 "
           + "WHERE id=$1", tuple)
           .map(result -> result.rowCount());
    }

    public void deleteRole(UUID roleId) {
        this.client.rxPreparedQuery("DELETE FROM roles WHERE id=$1", Tuple.of(roleId)).subscribe();
    }

    public Single<UserRoleDto> saveUserInRole(UserRoleDto userRole) {
        var tuple = Tuple.tuple();
        tuple.addUUID(userRole.getUserId());
        tuple.addUUID(userRole.getRoleId());
        return this.client.rxPreparedQuery("INSERT INTO user_role(user_id, role_id) "
           + "VALUES($1, $2)", tuple)
           .map(result -> userRole);
    }

    public Single<Integer> updateUserInRole(UserRoleDto userRole) {
        var tuple = Tuple.tuple();
        tuple.addUUID(userRole.getUserId());
        tuple.addUUID(userRole.getRoleId());
        // TODO this query makes no sense. Will revise when I get the chance
        return this.client.rxPreparedQuery("UPDATE user_role SET user_id=$1, role_id=$2 "
           + "WHERE user_id=$1 AND role_id=$2", tuple)
           .map(result -> result.rowCount());
    }

    public void deleteUserFromRole(UUID userId, UUID roleId) {
        this.client.rxPreparedQuery("DELETE FROM user_role WHERE user_id=$1 AND role_id=$2", Tuple.of(userId, roleId)).subscribe();
    }

    public Flowable<UserDto> findUsers(){
        // Query courtesy - https://dba.stackexchange.com/questions/173831/convert-right-side-of-join-of-many-to-many-into-array
        return this.client.rxBegin()
           .flatMapPublisher(tx -> tx.rxPrepare("SELECT id, username, email, first_name, last_name, enabled, created_timestamp, r.roles "
              + "FROM users u, "
              + "LATERAL( "
              + "SELECT ARRAY( "
              + "SELECT r.name FROM user_role ur "
              + "JOIN roles r ON r.id = ur.role_id "
              + "WHERE ur.user_id = u.id "
              + ") AS roles "
              + ") r" )
              .flatMapPublisher(preparedQuery -> {
                var stream = preparedQuery.createStream(50, Tuple.tuple());
                return stream.toFlowable();
              })
            .doAfterTerminate(tx::commit))
            .map(row -> {
                var user = new UserDto();
                user.setId(row.getUUID("id"));
                user.setEnabled(row.getBoolean("enabled"));
                user.setUsername(row.getString("username"));
                user.setEmail(row.getString("email"));
                user.setFirstName(row.getString("first_name"));
                user.setLastName(row.getString("last_name"));
                user.setCreatedTimestamp(row.getOffsetDateTime("created_timestamp").toInstant());
                user.setRoles(row.getStringArray("roles"));
                return user;
            });
    }
    
}