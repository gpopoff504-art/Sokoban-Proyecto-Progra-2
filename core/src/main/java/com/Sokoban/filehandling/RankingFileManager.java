/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban.filehandling;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gpopo
 */
public class RankingFileManager {

    private static final String RANKING_FILE = "data/ranking.dat";
    private static final int USERNAME_SIZE = 20;
    private static final int RECORD_SIZE = USERNAME_SIZE + 4 + 4; 

    public static void guardarOActualizar(String username, int score, int nivel) {
        File file = new File(RANKING_FILE);
        new File("data/").mkdirs();

        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            long totalRecords = raf.length() / RECORD_SIZE;

            for (long i = 0; i < totalRecords; i++) {
                raf.seek(i * RECORD_SIZE);
                byte[] usernameBytes = new byte[USERNAME_SIZE];
                raf.read(usernameBytes);
                String storedUsername = new String(usernameBytes).trim();
                int storedNivel = raf.readInt();
                int storedScore = raf.readInt();

                if (storedUsername.equals(username) && storedNivel == nivel) {
                    if (score > storedScore) {
                        raf.seek(i * RECORD_SIZE);
                        escribirRegistro(raf, username, nivel, score);
                    }
                    return;
                }
            }

            raf.seek(raf.length());
            escribirRegistro(raf, username, nivel, score);

        } catch (IOException e) {
            System.err.println("Error en ranking: " + e.getMessage());
        }
    }

    public static void anonimizarUsuario(String username) {
        File file = new File(RANKING_FILE);
        if (!file.exists()) return;

        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            long totalRecords = raf.length() / RECORD_SIZE;
            for (long i = 0; i < totalRecords; i++) {
                raf.seek(i * RECORD_SIZE);
                byte[] usernameBytes = new byte[USERNAME_SIZE];
                raf.read(usernameBytes);
                String storedUsername = new String(usernameBytes).trim();
                if (storedUsername.equals(username)) {
                    raf.seek(i * RECORD_SIZE);
                    byte[] anonBytes = new byte[USERNAME_SIZE];
                    byte[] src = "Usuario eliminado".getBytes();
                    System.arraycopy(src, 0, anonBytes, 0, Math.min(src.length, USERNAME_SIZE));
                    raf.write(anonBytes);
                }
            }
        } catch (IOException e) {
            System.err.println("RankingFileManager.anonimizarUsuario: " + e.getMessage());
        }
    }

    private static void escribirRegistro(RandomAccessFile raf, String username, int nivel, int score) throws IOException {
        byte[] usernameBytes = new byte[USERNAME_SIZE];
        byte[] src = username.getBytes();
        System.arraycopy(src, 0, usernameBytes, 0, Math.min(src.length, USERNAME_SIZE));
        raf.write(usernameBytes);
        raf.writeInt(nivel);
        raf.writeInt(score);
    }

    public static List<int[]> obtenerRankingGlobal() {
        List<int[]> registros = new ArrayList<>();
        File file = new File(RANKING_FILE);
        if (!file.exists()) return registros;

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            long totalRecords = raf.length() / RECORD_SIZE;
            for (long i = 0; i < totalRecords; i++) {
                raf.seek(i * RECORD_SIZE);
                byte[] usernameBytes = new byte[USERNAME_SIZE];
                raf.read(usernameBytes);
                String username = new String(usernameBytes).trim();
                int nivel = raf.readInt();
                int score = raf.readInt();
                registros.add(new int[]{nivel, score});
            }
        } catch (IOException e) {
            System.err.println("Error leyendo ranking: " + e.getMessage());
        }
        return registros;
    }

    public static List<String[]> obtenerTopGlobal(int top) {
        List<String[]> resultado = new ArrayList<>();
        File file = new File(RANKING_FILE);
        if (!file.exists()) return resultado;

        List<String> usernames = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            long totalRecords = raf.length() / RECORD_SIZE;
            for (long i = 0; i < totalRecords; i++) {
                raf.seek(i * RECORD_SIZE);
                byte[] usernameBytes = new byte[USERNAME_SIZE];
                raf.read(usernameBytes);
                String username = new String(usernameBytes).trim();
                raf.readInt();
                int score = raf.readInt();

                int idx = usernames.indexOf(username);
                if (idx >= 0) {
                    scores.set(idx, scores.get(idx) + score);
                } else {
                    usernames.add(username);
                    scores.add(score);
                }
            }
        } catch (IOException e) {
            System.err.println("Error leyendo ranking: " + e.getMessage());
        }

        for (int i = 0; i < usernames.size() - 1; i++) {
            for (int j = 0; j < usernames.size() - 1 - i; j++) {
                if (scores.get(j) < scores.get(j + 1)) {
                    int tmpScore = scores.get(j);
                    scores.set(j, scores.get(j + 1));
                    scores.set(j + 1, tmpScore);
                    String tmpUser = usernames.get(j);
                    usernames.set(j, usernames.get(j + 1));
                    usernames.set(j + 1, tmpUser);
                }
            }
        }

        int limite = Math.min(top, usernames.size());
        for (int i = 0; i < limite; i++) {
            resultado.add(new String[]{
                String.valueOf(i + 1),
                usernames.get(i),
                String.valueOf(scores.get(i))
            });
        }
        return resultado;
    }
}
