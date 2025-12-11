package com.gra.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {
    private int userId;
    private String name;
    private String email;
    private String password;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Preferenca preferenca;
    private Lokacion lokacion;
    private List<Role> roles;
    private List<Rezervim> rezervimet;
    private List<Pagesat> pagesat;
    private List<Vleresim> vleresimet;
    private List<Notifikime> notifikimet;
    private List<Kontakt> kontaktet;

    public User() {
        this.roles = new ArrayList<>();
        this.rezervimet = new ArrayList<>();
        this.pagesat = new ArrayList<>();
        this.vleresimet = new ArrayList<>();
        this.notifikimet = new ArrayList<>();
        this.kontaktet = new ArrayList<>();
    }

    public User(int userId, String name, String email, String password) {
        this();
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    public void logout() {
        // Implement logout logic
        System.out.println("User " + this.name + " logged out.");
    }

    public void updateProfile(String name, String email) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
        if (email != null && !email.isEmpty()) {
            this.email = email;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public Preferenca getPreferenca() {
        return preferenca;
    }

    public void setPreferenca(Preferenca preferenca) {
        this.preferenca = preferenca;
    }

    public void addRole(Role role) {
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }

    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(r -> r.getEmri().equalsIgnoreCase(roleName));
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Lokacion getLokacion() { return lokacion; }
    public void setLokacion(Lokacion lokacion) { this.lokacion = lokacion; }

    public List<Role> getRoles() { return roles; }
    public void setRoles(List<Role> roles) { this.roles = roles; }

    public List<Rezervim> getRezervimet() { return rezervimet; }
    public void setRezervimet(List<Rezervim> rezervimet) { this.rezervimet = rezervimet; }

    public List<Pagesat> getPagesat() { return pagesat; }
    public void setPagesat(List<Pagesat> pagesat) { this.pagesat = pagesat; }

    public List<Vleresim> getVleresimet() { return vleresimet; }
    public void setVleresimet(List<Vleresim> vleresimet) { this.vleresimet = vleresimet; }

    public List<Notifikime> getNotifikimet() { return notifikimet; }
    public void setNotifikimet(List<Notifikime> notifikimet) { this.notifikimet = notifikimet; }

    public List<Kontakt> getKontaktet() { return kontaktet; }
    public void setKontaktet(List<Kontakt> kontaktet) { this.kontaktet = kontaktet; }
}