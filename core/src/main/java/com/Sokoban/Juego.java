/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban;

/**
 *
 * @author Rogelio
 */
public abstract class Juego {
    protected int movimientos = 0;
    protected float tiempoTotal = 0;
    protected boolean gano = false;

    public abstract void mover(String direccion);
    public abstract boolean verificarVictoria();
    public abstract int calcularPuntaje();
    public abstract void update(float delta);
    public abstract void detener();

    public int getMovimientos() { return movimientos; }
    public float getTiempoTotal() { return tiempoTotal; }
    public boolean isGano() { return gano; }
}