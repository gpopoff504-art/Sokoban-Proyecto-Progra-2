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
public class StatsData implements Serializable {
    private static final long serialVersionUID = 2L;
    private int currentLevel;
    private int totalScore;
    private long totalTimePlayed;
    private int nivelesCompletados;
    private long tiempoTotalNiveles;
    private int[] mejoresPuntajesPorNivel;
    private List<String> gameHistory;

    // Stats de retos
    private int retosGanados;
    private int retosPerdidos;
    private int retosEmpatados;

    public StatsData() {
        this.currentLevel = 1;
        this.totalScore = 0;
        this.totalTimePlayed = 0;
        this.nivelesCompletados = 0;
        this.tiempoTotalNiveles = 0;
        this.mejoresPuntajesPorNivel = new int[6];
        this.gameHistory = new ArrayList<>();
        this.retosGanados = 0;
        this.retosPerdidos = 0;
        this.retosEmpatados = 0;
    }

    public int getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(int currentLevel) { this.currentLevel = currentLevel; }
    public int getTotalScore() { return totalScore; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }
    public long getTotalTimePlayed() { return totalTimePlayed; }
    public void setTotalTimePlayed(long totalTimePlayed) { this.totalTimePlayed = totalTimePlayed; }
    public int getNivelesCompletados() { return nivelesCompletados; }
    public void setNivelesCompletados(int n) { this.nivelesCompletados = n; }
    public long getTiempoTotalNiveles() { return tiempoTotalNiveles; }
    public void setTiempoTotalNiveles(long t) { this.tiempoTotalNiveles = t; }
    public int[] getMejoresPuntajesPorNivel() { return mejoresPuntajesPorNivel; }
    public List<String> getGameHistory() { return gameHistory; }
    public void addGameHistory(String entry) { this.gameHistory.add(entry); }

    public int getRetosGanados()   { return retosGanados; }
    public int getRetosPerdidos()  { return retosPerdidos; }
    public int getRetosEmpatados() { return retosEmpatados; }
    public void setRetosGanados(int n)   { this.retosGanados = n; }
    public void setRetosPerdidos(int n)  { this.retosPerdidos = n; }
    public void setRetosEmpatados(int n) { this.retosEmpatados = n; }
    public void addRetosGanados()   { this.retosGanados++; }
    public void addRetosPerdidos()  { this.retosPerdidos++; }
    public void addRetosEmpatados() { this.retosEmpatados++; }

    public int getMejorPuntajeNivel(int nivel) {
        if (nivel < 1 || nivel > 5) return 0;
        if (mejoresPuntajesPorNivel == null) mejoresPuntajesPorNivel = new int[6];
        return mejoresPuntajesPorNivel[nivel];
    }

    public boolean actualizarPuntajeNivel(int nivel, int nuevoPuntaje) {
        if (nivel < 1 || nivel > 5) return false;
        if (mejoresPuntajesPorNivel == null) mejoresPuntajesPorNivel = new int[6];
        int anterior = mejoresPuntajesPorNivel[nivel];
        if (nuevoPuntaje > anterior) {
            mejoresPuntajesPorNivel[nivel] = nuevoPuntaje;
            totalScore += (nuevoPuntaje - anterior);
            return true;
        }
        return false;
    }

    public double getTiempoPromedioPorNivel() {
        if (nivelesCompletados == 0) return 0;
        return (double) tiempoTotalNiveles / nivelesCompletados;
    }
}
