/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban;

/**
 *
 * @author Rogelio
 */
public class Tiempo extends Thread {

    private volatile boolean activo = false;
    private volatile boolean enEspera = true;
    private final Object monitor = new Object();
    private float acumulado = 0f;

    public Tiempo() {
        setDaemon(true);
    }

    @Override
    public void run() {
        activo = true;
        long marca = System.nanoTime();

        while (activo) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                break;
            }

            long ahora = System.nanoTime();
            float delta = (ahora - marca) / 1_000_000_000f;
            marca = ahora;

            synchronized (monitor) {
                if (!enEspera) {
                    acumulado += delta;
                }
            }
        }
    }

    public void activar() {
        synchronized (monitor) {
            enEspera = false;
        }
    }

    public void retener() {
        synchronized (monitor) {
            enEspera = true;
        }
    }

    public float consultar() {
        synchronized (monitor) {
            return acumulado;
        }
    }

    public void finalizar() {
        activo = false;
        this.interrupt();
    }
}