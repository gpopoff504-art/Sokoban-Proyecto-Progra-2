/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gpopo
 */
public class SocialData implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> friends;
    private List<String> solicitudesPendientes;

    public SocialData() {
        this.friends = new ArrayList<>();
        this.solicitudesPendientes = new ArrayList<>();
    }

    public List<String> getFriends() { return friends; }
    public void addFriend(String username) { friends.add(username); }
    public void removeFriend(String username) { friends.remove(username); }
    public List<String> getSolicitudesPendientes() { return solicitudesPendientes; }
    public void agregarSolicitud(String username) {
        if (!solicitudesPendientes.contains(username)) solicitudesPendientes.add(username);
    }
    public void eliminarSolicitud(String username) { solicitudesPendientes.remove(username); }
    public void setSolicitudesPendientes(List<String> solicitudes) { this.solicitudesPendientes = solicitudes; }
}
