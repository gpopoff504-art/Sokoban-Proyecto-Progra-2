/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban.filehandling;

import com.Sokoban.model.Player;
import com.Sokoban.model.ProfileData;
import com.Sokoban.model.StatsData;
import com.Sokoban.model.PrefsData;
import com.Sokoban.model.SocialData;
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

    private static final String USERS_ROOT   = "data/users/";
    private static final String PROFILE_FILE = "profile.dat";
    private static final String STATS_FILE   = "stats/stats.dat";
    private static final String PREFS_FILE   = "prefs/prefs.dat";
    private static final String SOCIAL_FILE  = "social/social.dat";

    public static boolean savePlayer(Player player) {
        String folder = USERS_ROOT + player.getUsername() + "/";
        new File(folder).mkdirs();
        new File(folder + "stats/").mkdirs();
        new File(folder + "prefs/").mkdirs();
        new File(folder + "social/").mkdirs();
        return saveObject(player.getProfile(), folder + PROFILE_FILE)
            && saveObject(player.getStats(),   folder + STATS_FILE)
            && saveObject(player.getPrefs(),   folder + PREFS_FILE)
            && saveObject(player.getSocial(),  folder + SOCIAL_FILE);
    }

    public static Player loadPlayer(String username) {
        String folder = USERS_ROOT + username + "/";
        if (!new File(folder + PROFILE_FILE).exists()) return null;

        ProfileData profile = (ProfileData) loadObject(folder + PROFILE_FILE);
        StatsData   stats   = (StatsData)   loadObject(folder + STATS_FILE);
        PrefsData   prefs   = (PrefsData)   loadObject(folder + PREFS_FILE);
        SocialData  social  = (SocialData)  loadObject(folder + SOCIAL_FILE);

        if (profile == null) return null;
        if (stats  == null) stats  = new StatsData();
        if (prefs  == null) prefs  = new PrefsData();
        if (social == null) social = new SocialData();

        Player player = new Player(profile.getUsername(), profile.getPassword(), profile.getFullName());
        player.getProfile().setAvatarPath(profile.getAvatarPath());
        player.getProfile().setLastSession(profile.getLastSession());
        player.getProfile().setActiva(profile.isActiva());

        player.getStats().setCurrentLevel(stats.getCurrentLevel());
        player.getStats().setTotalScore(stats.getTotalScore());
        player.getStats().setTotalTimePlayed(stats.getTotalTimePlayed());
        player.getStats().setNivelesCompletados(stats.getNivelesCompletados());
        player.getStats().setTiempoTotalNiveles(stats.getTiempoTotalNiveles());
        for (String h : stats.getGameHistory()) player.getStats().addGameHistory(h);
        for (int i = 1; i <= 5; i++) {
            int p = stats.getMejorPuntajeNivel(i);
            if (p > 0) player.getStats().actualizarPuntajeNivel(i, p);
        }

        player.getPrefs().setVolume(prefs.getVolume());
        player.getPrefs().setLanguage(prefs.getLanguage());

        for (String f : social.getFriends()) player.getSocial().addFriend(f);
        player.getSocial().setSolicitudesPendientes(social.getSolicitudesPendientes());

        return player;
    }

    public static boolean saveProfile(Player player) {
        String folder = USERS_ROOT + player.getUsername() + "/";
        new File(folder).mkdirs();
        return saveObject(player.getProfile(), folder + PROFILE_FILE);
    }

    public static boolean saveStats(Player player) {
        String folder = USERS_ROOT + player.getUsername() + "/";
        new File(folder + "stats/").mkdirs();
        return saveObject(player.getStats(), folder + STATS_FILE);
    }

    public static boolean savePrefs(Player player) {
        String folder = USERS_ROOT + player.getUsername() + "/";
        new File(folder + "prefs/").mkdirs();
        return saveObject(player.getPrefs(), folder + PREFS_FILE);
    }

    public static boolean saveSocial(Player player) {
        String folder = USERS_ROOT + player.getUsername() + "/";
        new File(folder + "social/").mkdirs();
        return saveObject(player.getSocial(), folder + SOCIAL_FILE);
    }

    public static boolean playerExists(String username) {
        return new File(USERS_ROOT + username + "/" + PROFILE_FILE).exists();
    }

    public static boolean renameUserFolder(String oldUsername, String newUsername) {
        File oldFolder = new File(USERS_ROOT + oldUsername + "/");
        File newFolder = new File(USERS_ROOT + newUsername + "/");
        if (!oldFolder.exists()) return false;
        if (newFolder.exists()) return false;
        boolean ok = oldFolder.renameTo(newFolder);
        if (!ok) System.err.println("No se pudo renombrar la carpeta.");
        return ok;
    }

    public static boolean deletePlayer(String username) {
        File folder = new File(USERS_ROOT + username + "/");
        if (!folder.exists()) return false;

        RankingFileManager.anonimizarUsuario(username);
        ChallengeFileManager.borrarRetosDeUsuario(username);

        borrarRecursivo(folder);
        return true;
    }

    private static void borrarRecursivo(File archivo) {
        if (archivo.isDirectory()) {
            for (File hijo : archivo.listFiles()) {
                borrarRecursivo(hijo);
            }
        }
        archivo.delete();
    }

    private static boolean saveObject(Object obj, String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(obj);
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar " + path + ": " + e.getMessage());
            return false;
        }
    }

    private static Object loadObject(String path) {
        File file = new File(path);
        if (!file.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al leer " + path + ": " + e.getMessage());
            return null;
        }
    }

    public static boolean createUserFolder(String username) {
        File folder = new File(USERS_ROOT + username + "/");
        if (!folder.exists()) return folder.mkdirs();
        return true;
    }
}
