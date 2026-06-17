/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban.filehandling;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gpopo
 */
public class ChallengeFileManager {

    private static final String FILE = "data/challenges.dat";
    private static final int USER_SIZE  = 20;
    private static final int REC_SIZE   = USER_SIZE * 2 + 4 * 4;
    public static final int PENDIENTE   = 0;
    public static final int ACEPTADO    = 1;
    public static final int COMPLETADO  = 2;
    public static final int RECHAZADO   = 3;

    public static boolean enviarReto(String retador, String retado, int nivel) {
        new File("data/").mkdirs();
        try (RandomAccessFile raf = new RandomAccessFile(FILE, "rw")) {
            long total = raf.length() / REC_SIZE;
            for (long i = 0; i < total; i++) {
                raf.seek(i * REC_SIZE);
                String r = readStr(raf, USER_SIZE);
                String d = readStr(raf, USER_SIZE);
                int    n = raf.readInt();
                raf.readInt(); raf.readInt();
                int estado = raf.readInt();
                if (r.equals(retador) && d.equals(retado)
                        && n == nivel && estado == PENDIENTE) return false;
            }
            raf.seek(raf.length());
            writeStr(raf, retador, USER_SIZE);
            writeStr(raf, retado, USER_SIZE);
            raf.writeInt(nivel);
            raf.writeInt(0);
            raf.writeInt(0);
            raf.writeInt(PENDIENTE);
            return true;
        } catch (IOException e) {
            System.err.println("ChallengeFileManager.enviarReto: " + e.getMessage());
            return false;
        }
    }

    public static void borrarRetosDeUsuario(String username) {
        File f = new File(FILE);
        if (!f.exists()) return;

        List<byte[]> registrosRestantes = new ArrayList<>();

        try (RandomAccessFile raf = new RandomAccessFile(FILE, "r")) {
            long total = raf.length() / REC_SIZE;
            for (long i = 0; i < total; i++) {
                raf.seek(i * REC_SIZE);
                byte[] rec = new byte[REC_SIZE];
                raf.readFully(rec);

                String retador = new String(rec, 0, USER_SIZE).trim();
                String retado  = new String(rec, USER_SIZE, USER_SIZE).trim();
                int estado = bytesToInt(rec, USER_SIZE * 2 + 4 * 3);

                boolean involucrado = retador.equals(username) || retado.equals(username);
                boolean activo = estado == PENDIENTE || estado == ACEPTADO;

                if (!(involucrado && activo)) {
                    registrosRestantes.add(rec);
                }
            }
        } catch (IOException e) {
            System.err.println("ChallengeFileManager.borrarRetosDeUsuario (lectura): " + e.getMessage());
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(FILE, false)) {
            for (byte[] rec : registrosRestantes) {
                fos.write(rec);
            }
        } catch (IOException e) {
            System.err.println("ChallengeFileManager.borrarRetosDeUsuario (escritura): " + e.getMessage());
        }
    }

    private static int bytesToInt(byte[] b, int offset) {
        return ((b[offset] & 0xFF) << 24)
             | ((b[offset+1] & 0xFF) << 16)
             | ((b[offset+2] & 0xFF) << 8)
             |  (b[offset+3] & 0xFF);
    }

    public static List<long[]> getRetosParaUsuario(String username) {
        List<long[]> lista = new ArrayList<>();
        File f = new File(FILE);
        if (!f.exists()) return lista;
        try (RandomAccessFile raf = new RandomAccessFile(FILE, "r")) {
            long total = raf.length() / REC_SIZE;
            for (long i = 0; i < total; i++) {
                raf.seek(i * REC_SIZE);
                String retador = readStr(raf, USER_SIZE);
                String retado  = readStr(raf, USER_SIZE);
                int nivel      = raf.readInt();
                int sRet       = raf.readInt();
                int sRtd       = raf.readInt();
                int estado     = raf.readInt();
                if (retado.equals(username) && estado == PENDIENTE) {
                    lista.add(new long[]{i, nivel, sRet, sRtd, estado});
                }
            }
        } catch (IOException e) {
            System.err.println("ChallengeFileManager.getRetosParaUsuario: " + e.getMessage());
        }
        return lista;
    }

    public static List<long[]> getRetosEnviados(String username) {
        List<long[]> lista = new ArrayList<>();
        File f = new File(FILE);
        if (!f.exists()) return lista;
        try (RandomAccessFile raf = new RandomAccessFile(FILE, "r")) {
            long total = raf.length() / REC_SIZE;
            for (long i = 0; i < total; i++) {
                raf.seek(i * REC_SIZE);
                String retador = readStr(raf, USER_SIZE);
                String retado  = readStr(raf, USER_SIZE);
                int nivel      = raf.readInt();
                int sRet       = raf.readInt();
                int sRtd       = raf.readInt();
                int estado     = raf.readInt();
                if (retador.equals(username) && estado != RECHAZADO) {
                    lista.add(new long[]{i, nivel, sRet, sRtd, estado});
                }
            }
        } catch (IOException e) {
            System.err.println("ChallengeFileManager.getRetosEnviados: " + e.getMessage());
        }
        return lista;
    }

    public static String getRetador(long idx) { return readField(idx, 0); }
    public static String getRetado(long idx)  { return readField(idx, USER_SIZE); }

    private static String readField(long idx, int offset) {
        File f = new File(FILE);
        if (!f.exists()) return "";
        try (RandomAccessFile raf = new RandomAccessFile(FILE, "r")) {
            raf.seek(idx * REC_SIZE + offset);
            return readStr(raf, USER_SIZE);
        } catch (IOException e) { return ""; }
    }

    public static boolean actualizarEstado(long idx, int nuevoEstado) {
        try (RandomAccessFile raf = new RandomAccessFile(FILE, "rw")) {
            raf.seek(idx * REC_SIZE + USER_SIZE * 2 + 4 * 3);
            raf.writeInt(nuevoEstado);
            return true;
        } catch (IOException e) {
            System.err.println("ChallengeFileManager.actualizarEstado: " + e.getMessage());
            return false;
        }
    }

    public static boolean guardarScoreRetador(long idx, int score) {
        try (RandomAccessFile raf = new RandomAccessFile(FILE, "rw")) {
            raf.seek(idx * REC_SIZE + USER_SIZE * 2 + 4);
            raf.writeInt(score);
            return true;
        } catch (IOException e) { return false; }
    }

    public static boolean guardarScoreRetado(long idx, int score) {
        try (RandomAccessFile raf = new RandomAccessFile(FILE, "rw")) {
            raf.seek(idx * REC_SIZE + USER_SIZE * 2 + 4 * 2);
            raf.writeInt(score);
            return true;
        } catch (IOException e) { return false; }
    }

    private static String readStr(RandomAccessFile raf, int size) throws IOException {
        byte[] b = new byte[size];
        raf.read(b);
        return new String(b).trim();
    }

    private static void writeStr(RandomAccessFile raf, String s, int size) throws IOException {
        byte[] b = new byte[size];
        byte[] src = s.getBytes();
        System.arraycopy(src, 0, b, 0, Math.min(src.length, size));
        raf.write(b);
    }
    
    public static int[] getH2H(String userA, String userB) {
        int[] result = {0, 0, 0}; // [ganaA, ganaB, empates]
        File f = new File(FILE);
        if (!f.exists()) return result;
        try (RandomAccessFile raf = new RandomAccessFile(FILE, "r")) {
            long total = raf.length() / REC_SIZE;
            for (long i = 0; i < total; i++) {
                raf.seek(i * REC_SIZE);
                String retador = readStr(raf, USER_SIZE);
                String retado  = readStr(raf, USER_SIZE);
                int nivel      = raf.readInt();
                int sRetador   = raf.readInt();
                int sRetado    = raf.readInt();
                int estado     = raf.readInt();
                if (estado != COMPLETADO) continue;
                boolean match1 = retador.equals(userA) && retado.equals(userB);
                boolean match2 = retador.equals(userB) && retado.equals(userA);
                if (!match1 && !match2) continue;
                int scoreA = match1 ? sRetador : sRetado;
                int scoreB = match1 ? sRetado  : sRetador;
                if (scoreA > scoreB) result[0]++;
                else if (scoreB > scoreA) result[1]++;
                else result[2]++;
            }
        } catch (IOException e) {
            System.err.println("ChallengeFileManager.getH2H: " + e.getMessage());
        }
        return result;
    }

}
