/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban.filehandling;

import com.Sokoban.model.Player;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author gpopo
 */
public class FileManager {

    private static final String USERS_ROOT = "data/users/";
    private static final String PLAYER_FILE = "player.dat";

    public static boolean savePlayer(Player player) {
        String folderPath = USERS_ROOT + player.getUsername() + "/";
        File folder = new File(folderPath);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(folderPath + PLAYER_FILE);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(player);
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar el jugador: " + e.getMessage());
            return false;
        }
    }


    public static Player loadPlayer(String username) {
        String filePath = USERS_ROOT + username + "/" + PLAYER_FILE;
        File file = new File(filePath);

        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Player) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al leer el jugador: " + e.getMessage());
            return null;
        }
    }


    public static boolean playerExists(String username) {
        String filePath = USERS_ROOT + username + "/" + PLAYER_FILE;
        return new File(filePath).exists();
    }

    public static boolean deletePlayer(String username) {
        File folder = new File(USERS_ROOT + username + "/");
        if (!folder.exists()) {
            return false;
        }
        for (File file : folder.listFiles()) {
            file.delete();
        }
        return folder.delete();
    }


    public static boolean createUserFolder(String username) {
        File folder = new File(USERS_ROOT + username + "/");
        if (!folder.exists()) {
            return folder.mkdirs();
        }
        return true;
    }
}
