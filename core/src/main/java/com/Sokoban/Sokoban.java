/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban;

import com.Sokoban.model.Level;
import com.Sokoban.model.Jugador;
import java.util.ArrayDeque;

/**
 *
 * @author Rogelio
 */
public class Sokoban extends Juego {

    private Level nivelActual;
    private Jugador jugador;
    private Tiempo worker;
    private ArrayDeque<int[][]> historialMapas;
    private ArrayDeque<int[]> historialJugador;

    public Sokoban(Level nivelActual, Jugador jugador) {
        this.nivelActual = nivelActual;
        this.jugador = jugador;
        this.worker = new Tiempo();
        this.historialMapas = new ArrayDeque<>();
        this.historialJugador = new ArrayDeque<>();
        this.worker.start();
    }

    private int[][] copiarMapa(int[][] mapa) {
        int[][] copia = new int[mapa.length][mapa[0].length];
        for (int f = 0; f < mapa.length; f++) {
            for (int c = 0; c < mapa[0].length; c++) {
                copia[f][c] = mapa[f][c];
            }
        }
        return copia;
    }

    private void guardarEstado() {
        historialMapas.push(copiarMapa(nivelActual.getMapa()));
        historialJugador.push(new int[]{jugador.getGridX(), jugador.getGridY()});
    }

    public void undo() {
        if (historialMapas.isEmpty()) return;
        int[][] mapaAnterior = historialMapas.pop();
        int[] posAnterior = historialJugador.pop();
        int[][] mapa = nivelActual.getMapa();
        for (int f = 0; f < mapa.length; f++) {
            for (int c = 0; c < mapa[0].length; c++) {
                mapa[f][c] = mapaAnterior[f][c];
            }
        }
        jugador.setGridX(posAnterior[0]);
        jugador.setGridY(posAnterior[1]);
        jugador.setIdle();
        movimientos++;
    }

    @Override
    public void mover(String dir) {
        if (gano) return;
        if (movimientos == 0) {
            worker.activar();
        }
        guardarEstado();
        int dx = 0, dy = 0;
        switch (dir) {
            case "left":  dx = -1; break;
            case "right": dx = 1;  break;
            case "up":    dy = -1; break;
            case "down":  dy = 1;  break;
            default: return;
        }
        moverJugador(dx, dy, dir);
    }

    private void moverJugador(int dx, int dy, String dir) {
        int[][] mapa = nivelActual.getMapa();
        int px = jugador.getGridX();
        int py = jugador.getGridY();
        int nx = px + dx;
        int ny = py + dy;
        if (nx < 0 || ny < 0 || ny >= nivelActual.getFilas() || nx >= nivelActual.getColumnas()) {
            historialMapas.pop();
            historialJugador.pop();
            return;
        }
        int celda = mapa[ny][nx];
        if (celda == Level.MURO || celda == Level.VACIO) {
            jugador.setIdle();
            historialMapas.pop();
            historialJugador.pop();
            return;
        }
        if (celda == Level.CAJA) {
            int cx = nx + dx;
            int cy = ny + dy;
            if (cx < 0 || cy < 0 || cy >= nivelActual.getFilas() || cx >= nivelActual.getColumnas()) {
                historialMapas.pop();
                historialJugador.pop();
                return;
            }
            int detras = mapa[cy][cx];
            if (detras == Level.MURO || detras == Level.CAJA || detras == Level.VACIO) {
                jugador.setIdle();
                historialMapas.pop();
                historialJugador.pop();
                return;
            }
            mapa[cy][cx] = Level.CAJA;
            mapa[ny][nx] = (nivelActual.getMapaObjetivos()[ny][nx] == Level.OBJETIVO) ? Level.OBJETIVO : Level.PISO;
            jugador.setMoviendo(dir, true);
        } else {
            jugador.setMoviendo(dir, false);
        }
        jugador.setGridX(nx);
        jugador.setGridY(ny);
        movimientos++;
    }

    @Override
    public void resetear() {
        historialMapas.clear();
        historialJugador.clear();
    }

    @Override
    public boolean verificarVictoria() {
        int[][] mapa = nivelActual.getMapa();
        int[][] mapaOriginal = nivelActual.getMapaObjetivos();
        for (int f = 0; f < nivelActual.getFilas(); f++) {
            for (int c = 0; c < nivelActual.getColumnas(); c++) {
                if (mapaOriginal[f][c] == Level.OBJETIVO && mapa[f][c] != Level.CAJA) {
                    return false;
                }
            }
        }
        gano = true;
        worker.retener();
        return true;
    }

    @Override
    public int calcularPuntaje() {
        return Math.max(100, 1000 - (movimientos * 10) - ((int) tiempoTotal * 5));
    }

    @Override
    public void update(float delta) {
        tiempoTotal = worker.consultar();
        jugador.update(delta);
    }

    @Override
    public void detener() {
        worker.finalizar();
    }

    public Level getNivelActual() { return nivelActual; }
    public Jugador getJugador() { return jugador; }
}