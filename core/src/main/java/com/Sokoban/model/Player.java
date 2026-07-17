/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author gpopo
 */
public class Player implements Serializable {

    private static final long serialVersionUID = 2L;

    private ProfileData profile;
    private StatsData stats;
    private PrefsData prefs;
    private SocialData social;

    public Player(String username, String password, String fullName) {
        this.profile = new ProfileData(username, password, fullName);
        this.stats   = new StatsData();
        this.prefs   = new PrefsData();
        this.social  = new SocialData();
    }

    // Getters de los 4 objetos internos
    public ProfileData getProfile() { return profile; }
    public StatsData getStats()     { return stats; }
    public PrefsData getPrefs()     { return prefs; }
    public SocialData getSocial()   { return social; }

    // ── Profile ──
    public String getUsername()               { return profile.getUsername(); }
    public void setUsername(String u)         { profile.setUsername(u); }
    public String getPassword()               { return profile.getPassword(); }
    public void setPassword(String p)         { profile.setPassword(p); }
    public String getFullName()               { return profile.getFullName(); }
    public void setFullName(String f)         { profile.setFullName(f); }
    public String getAvatarPath()             { return profile.getAvatarPath(); }
    public void setAvatarPath(String a)       { profile.setAvatarPath(a); }
    public LocalDateTime getRegistrationDate(){ return profile.getRegistrationDate(); }
    public LocalDateTime getLastSession()     { return profile.getLastSession(); }
    public void setLastSession(LocalDateTime d){ profile.setLastSession(d); }
    public boolean isActiva()                 { return profile.isActiva(); }
    public void setActiva(boolean a)          { profile.setActiva(a); }

    // ── Stats ──
    public int getCurrentLevel()              { return stats.getCurrentLevel(); }
    public void setCurrentLevel(int l)        { stats.setCurrentLevel(l); }
    public int getTotalScore()                { return stats.getTotalScore(); }
    public void setTotalScore(int s)          { stats.setTotalScore(s); }
    public long getTotalTimePlayed()          { return stats.getTotalTimePlayed(); }
    public void setTotalTimePlayed(long t)    { stats.setTotalTimePlayed(t); }
    public int getNivelesCompletados()        { return stats.getNivelesCompletados(); }
    public void setNivelesCompletados(int n)  { stats.setNivelesCompletados(n); }
    public long getTiempoTotalNiveles()       { return stats.getTiempoTotalNiveles(); }
    public void setTiempoTotalNiveles(long t) { stats.setTiempoTotalNiveles(t); }
    public double getTiempoPromedioPorNivel() { return stats.getTiempoPromedioPorNivel(); }
    public List<String> getGameHistory()      { return stats.getGameHistory(); }
    public void addGameHistory(String entry)  { stats.addGameHistory(entry); }
    public int getMejorPuntajeNivel(int n)    { return stats.getMejorPuntajeNivel(n); }
    public boolean actualizarPuntajeNivel(int nivel, int puntaje) {
        return stats.actualizarPuntajeNivel(nivel, puntaje);
    }

    // ── Prefs ──
    public float getVolume()                  { return prefs.getVolume(); }
    public void setVolume(float v)            { prefs.setVolume(v); }
    public Language getLanguage()             { return prefs.getLanguage(); }
    public void setLanguage(Language l)       { prefs.setLanguage(l); }

    // ── Social ──
    public List<String> getFriends()          { return social.getFriends(); }
    public void addFriend(String u)           { social.addFriend(u); }
    public void removeFriend(String u)        { social.removeFriend(u); }
    public List<String> getSolicitudesPendientes() { return social.getSolicitudesPendientes(); }
    public void agregarSolicitud(String u)    { social.agregarSolicitud(u); }
    public void eliminarSolicitud(String u)   { social.eliminarSolicitud(u); }
    public void setSolicitudesPendientes(List<String> s) { social.setSolicitudesPendientes(s); }

    @Override
    public String toString() {
        return "Player{username='" + getUsername() + "', currentLevel=" + getCurrentLevel() +
               ", totalScore=" + getTotalScore() + "}";
    }
}
