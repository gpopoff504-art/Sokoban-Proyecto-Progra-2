/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban.model;

/**
 *
 * @author Rogelio
 */
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Level {

    protected int[][] mapa;
    protected int filas;
    protected int columnas;
    protected int jugadorX;
    protected int jugadorY;

    public static final int VACIO = 0;
    public static final int MURO = 1;
    public static final int PISO = 2;
    public static final int OBJETIVO = 3;
    public static final int CAJA = 4;
    public static final int JUGADOR = 5;

    public abstract void cargarMapa(String ruta);
    public abstract void render(SpriteBatch batch, int tileSize, int offsetX, int offsetY);
    public abstract String getNombre();
    public abstract int getNivel();

    public int[][] getMapa(){ 
        return mapa;
    }
    public int getFilas(){ 
        return filas; 
    }
    public int getColumnas(){ 
        return columnas; 
    }
    public int getJugadorX(){ 
        return jugadorX; 
    }
    public int getJugadorY(){ 
        return jugadorY; 
    }
    public void setJugadorX(int x){
        this.jugadorX = x; 
    }
    public void setJugadorY(int y){ 
        this.jugadorY = y;
    }
}