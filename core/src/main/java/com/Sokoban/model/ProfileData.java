/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author gpopo
 */
public class ProfileData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String fullName;
    private String avatarPath;
    private LocalDateTime registrationDate;
    private LocalDateTime lastSession;
    private boolean activa;

    public ProfileData(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.registrationDate = LocalDateTime.now();
        this.lastSession = LocalDateTime.now();
        this.avatarPath = "default";
        this.activa = true;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getAvatarPath() { return avatarPath; }
    public void setAvatarPath(String avatarPath) { this.avatarPath = avatarPath; }
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public LocalDateTime getLastSession() { return lastSession; }
    public void setLastSession(LocalDateTime lastSession) { this.lastSession = lastSession; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
}
