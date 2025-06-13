package com.app.musicapp.model.response;

import java.util.Set;

public class UserResponse {
    String id;
    String username;
    Set<RoleResponse> roles;

    public UserResponse(String id, String username, Set<RoleResponse> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<RoleResponse> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleResponse> roles) {
        this.roles = roles;
    }
}
