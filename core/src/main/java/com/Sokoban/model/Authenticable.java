/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.Sokoban.model;

/**
 *
 * @author gpopo
 */
public interface Authenticable {
    boolean register(String username, String password, String fullName);
    boolean login(String username, String password);
    boolean logout(String username);
    boolean changePassword(String username, String oldPassword, String newPassword);
    boolean validatePassword(String password);
}
