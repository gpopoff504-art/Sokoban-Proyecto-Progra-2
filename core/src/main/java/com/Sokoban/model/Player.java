/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gpopo
 */
public class Player implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int nivelesCompletados = 0;
    private long tiempoTotalNiveles = 0;
    
    private String username;
    private String password;
    private String fullName;
    private String avatarPath;
    
    private LocalDateTime registrationDate;
    private LocalDateTime lastSession;
    
    private int currentLevel;
    private int totalScore;
    private long totalTimePlayed;
    private List<String> gameHistory;
    
    private float volume;
    private Language language;
    
    private List<String> friends;
    private List<String> solicitudesPendientes;

    private boolean activa = true;

    public Player(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.registrationDate = LocalDateTime.now();
        this.lastSession = LocalDateTime.now();
        this.currentLevel = 1;
        this.totalScore = 0;
        this.totalTimePlayed = 0;
        this.gameHistory = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.volume = 1.0f;
        this.language = Language.SPANISH;
        this.avatarPath = "default";
        this.solicitudesPendientes = new ArrayList<>();
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
    
    public int getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(int currentLevel) { this.currentLevel = currentLevel; }
    
    public int getTotalScore() { return totalScore; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }
    public int getNivelesCompletados(){ return nivelesCompletados; }
    public void setNivelesCompletados(int n){ this.nivelesCompletados = n; }
    public long getTiempoTotalNiveles(){ return tiempoTotalNiveles; }
    public void setTiempoTotalNiveles(long t){ this.tiempoTotalNiveles = t; }
    public double getTiempoPromedioPorNivel(){
        if(nivelesCompletados == 0) return 0;
    return (double) tiempoTotalNiveles / nivelesCompletados;
}
    public long getTotalTimePlayed() { return totalTimePlayed; }
    public void setTotalTimePlayed(long totalTimePlayed) { this.totalTimePlayed = totalTimePlayed; }
    
    public List<String> getGameHistory() { return gameHistory; }
    public void addGameHistory(String entry) { this.gameHistory.add(entry); }
    
    public float getVolume() { return volume; }
    public void setVolume(float volume) { this.volume = volume; }
    
    public Language getLanguage() { return language; }
    public void setLanguage(Language language) { this.language = language; }
    
    public List<String> getFriends() { return friends; }
    public void addFriend(String friendUsername) { this.friends.add(friendUsername); }
    public void removeFriend(String friendUsername) { this.friends.remove(friendUsername); }
    
    public boolean isActiva(){ return activa; }
    public void setActiva(boolean activa){ this.activa = activa; }

    public List<String> getSolicitudesPendientes(){ return solicitudesPendientes; }
    public void agregarSolicitud(String username){ 
        if(!solicitudesPendientes.contains(username)) 
            solicitudesPendientes.add(username); 
    }
    public void eliminarSolicitud(String username){ solicitudesPendientes.remove(username); }
    public void setSolicitudesPendientes(java.util.List<String> solicitudes){ this.solicitudesPendientes = solicitudes; }

    @Override
    public String toString() {
        return "Player{username='" + username + "', fullName='" + fullName + 
               "', currentLevel=" + currentLevel + ", totalScore=" + totalScore + "}";
    }
}
