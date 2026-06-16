/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban.model;

/**
 *
 * @author Rogelio
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Level{

    protected int[][] mapa;
    protected int[][] mapaObjetivos;
    protected int filas;
    protected int columnas;
    protected int jugadorX;
    protected int jugadorY;

    protected Texture texMuro;
    protected Texture texPiso;
    protected Texture texObjetivo;
    protected Texture texCaja;

    public static final int VACIO = 0;
    public static final int MURO = 1;
    public static final int PISO = 2;
    public static final int OBJETIVO = 3;
    public static final int CAJA = 4;
    public static final int JUGADOR = 5;

    protected abstract String getRutaMapa();
    protected abstract String getRutaMuro();
    protected abstract String getRutaPiso();
    protected abstract String getRutaObjetivo();
    public abstract String getNombre();
    public abstract int getNivel();

    protected void cargarSprites(){
        texMuro = new Texture(Gdx.files.internal(getRutaMuro()));
        texPiso = new Texture(Gdx.files.internal(getRutaPiso()));
        texObjetivo = new Texture(Gdx.files.internal(getRutaObjetivo()));
        texCaja = new Texture(Gdx.files.internal("sprites/Mapa/caja.png"));
    }

    public void cargarMapa(){
        String texto = Gdx.files.internal(getRutaMapa()).readString();
        String[] lineas = texto.split("\n");
        filas = lineas.length;
        columnas = 0;
        for(String linea : lineas){
            if(linea.trim().length() > columnas){
                columnas = linea.trim().length();
            }
        }
        mapa = new int[filas][columnas];
        for(int f = 0; f < filas; f++){
            String linea = lineas[f].trim();
            for(int c = 0; c < linea.length(); c++){
                char ch = linea.charAt(c);
                switch(ch){
                    case '#': mapa[f][c] = MURO; break;
                    case '.': mapa[f][c] = PISO; break;
                    case 'X': mapa[f][c] = OBJETIVO; break;
                    case '@':
                        mapa[f][c] = PISO;
                        jugadorX = c;
                        jugadorY = f;
                        break;
                    case '$': mapa[f][c] = CAJA; break;
                    default: mapa[f][c] = VACIO; break;
                }
            }
        }
        guardarObjetivos();
    }

    public void render(SpriteBatch batch, int tileSize, int offsetX, int offsetY){
        for(int f = 0; f < filas; f++){
            for(int c = 0; c < columnas; c++){
                int screenX = offsetX + c * tileSize;
                int screenY = offsetY + (filas - 1 - f) * tileSize;
                switch(mapa[f][c]){
                    case MURO: batch.draw(texMuro, screenX, screenY, tileSize, tileSize); break;
                    case PISO: batch.draw(texPiso, screenX, screenY, tileSize, tileSize); break;
                    case OBJETIVO: batch.draw(texObjetivo, screenX, screenY, tileSize, tileSize); break;
                    case CAJA:
                        batch.draw(texPiso, screenX, screenY, tileSize, tileSize);
                        batch.draw(texCaja, screenX, screenY, tileSize, tileSize);
                        break;
                }
            }
        }
    }

    protected void guardarObjetivos(){
        mapaObjetivos = new int[filas][columnas];
        for(int f = 0; f < filas; f++){
            for(int c = 0; c < columnas; c++){
                mapaObjetivos[f][c] = mapa[f][c];
            }
        }
    }

    public void dispose(){
        texMuro.dispose();
        texPiso.dispose();
        texObjetivo.dispose();
        texCaja.dispose();
    }

    public int[][] getMapaObjetivos(){ return mapaObjetivos; }
    public int[][] getMapa(){ return mapa; }
    public int getFilas(){ return filas; }
    public int getColumnas(){ return columnas; }
    public int getJugadorX(){ return jugadorX; }
    public int getJugadorY(){ return jugadorY; }
    public void setJugadorX(int x){ this.jugadorX = x; }
    public void setJugadorY(int y){ this.jugadorY = y; }
}