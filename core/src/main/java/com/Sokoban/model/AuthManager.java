package com.Sokoban.model;

import com.Sokoban.filehandling.FileManager;

public class AuthManager implements Authenticable {

    private static Player currentPlayer = null;

    @Override
    public boolean register(String username, String password, String fullName, String avatar){
        if(username == null || username.trim().isEmpty()) return false;
        if(!validatePassword(password)) return false;
        if(FileManager.playerExists(username)) return false;
        Player newPlayer = new Player(username, password, fullName);
        newPlayer.setAvatarPath(avatar);
        boolean guardado = FileManager.savePlayer(newPlayer);
        if(guardado){
            currentPlayer = newPlayer;
        }
        return guardado;
    }

    @Override
    public boolean login(String username, String password) {
        if (!FileManager.playerExists(username)) return false;

        Player player = FileManager.loadPlayer(username);
        if (player == null) return false;

        if (player.getPassword().equals(password)) {
            currentPlayer = player;
            currentPlayer.setLastSession(java.time.LocalDateTime.now());
            FileManager.savePlayer(currentPlayer);
            if(currentPlayer.getCurrentLevel() > 5){
                currentPlayer.setCurrentLevel(5);
                FileManager.savePlayer(currentPlayer);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean logout(String username) {
        if (currentPlayer != null && currentPlayer.getUsername().equals(username)) {
            FileManager.savePlayer(currentPlayer); 
            currentPlayer = null;
            return true;
        }
        return false;
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Player player = FileManager.loadPlayer(username);
        if (player == null) return false;
        if (!player.getPassword().equals(oldPassword)) return false;
        if (!validatePassword(newPassword)) return false;

        player.setPassword(newPassword);
        return FileManager.savePlayer(player);
    }

    @Override
    public boolean validatePassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    public static void setCurrentPlayer(Player player){
        currentPlayer = player;
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }
}