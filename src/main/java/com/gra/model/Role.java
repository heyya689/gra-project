package com.gra.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Role {
    private int roleId;
    private String emri;
    private String description;
    private String permissions;
    private LocalDateTime createdAt;
    private List<User> users;

    public Role() {
        this.users = new ArrayList<>();
    }

    public Role(int roleId, String emri) {
        this();
        this.roleId = roleId;
        this.emri = emri;
    }

    public void assignToUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
            user.addRole(this);
        }
    }

    public void removeFromUser(User user) {
        users.remove(user);
        user.removeRole(this);
    }

    public boolean hasPermission(String permission) {
        if (permissions == null) return false;
        return permissions.contains(permission);
    }

    // Getters and Setters
    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public String getEmri() { return emri; }
    public void setEmri(String emri) { this.emri = emri; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPermissions() { return permissions; }
    public void setPermissions(String permissions) { this.permissions = permissions; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }
}