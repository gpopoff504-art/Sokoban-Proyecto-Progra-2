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

public class Level1 extends Level {
    private Texture texMuro;
    private Texture texPiso;
    private Texture texObjetivo;
    private Texture texCaja;

    public Level1(){
        texMuro = new Texture(Gdx.files.internal("sprites/Mapa/m1.png"));
        texPiso = new Texture(Gdx.files.internal("sprites/Mapa/p1.png"));
        texObjetivo = new Texture(Gdx.files.internal("sprites/Mapa/pc1.png"));
        texCaja = new Texture(Gdx.files.internal("sprites/Mapa/caja.png"));
        cargarMapa("levels/level1.txt");
    }

    @Override
    public void cargarMapa(String ruta){
        String texto = Gdx.files.internal(ruta).readString();
        String[] lineas = texto.split("\n");

        filas = lineas.length;
        columnas = 0;
        
        for(String linea : lineas){
            if(linea.trim().length() > columnas){
                columnas = linea.trim().length();
            }
        }

        mapa = new int[filas][columnas];

        for(int x = 0; x < filas; x++){
            String linea = lineas[x].trim();
            for(int y = 0; y < linea.length(); y++){
                char ch = linea.charAt(y);
                switch(ch){
                    
                    case '#': mapa[x][y] = MURO;     
                    break;
                    
                    case '.': mapa[x][y] = PISO;    
                    break;
                    
                    case 'X': mapa[x][y] = OBJETIVO; 
                    break;
                    
                    case '@':
                        mapa[x][y] = PISO;
                        jugadorX = y;
                        jugadorY = x;
                        break;
                        
                    case '$':
                        mapa[x][y] = CAJA;
                        break;
                        
                    default:  mapa[x][y] = VACIO;    
                    break;
                    
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, int tileSize, int offsetX, int offsetY){
        for(int f = 0; f < filas; f++) {
            for(int c = 0; c < columnas; c++){
                int screenX = offsetX + c * tileSize;
                int screenY = offsetY + (filas - 1 - f) * tileSize;
                switch (mapa[f][c]){
                    case MURO: batch.draw(texMuro, screenX, screenY, tileSize, tileSize);
                    break;
                    
                    case PISO: batch.draw(texPiso, screenX, screenY, tileSize, tileSize); 
                    break;
                    
                    case OBJETIVO: batch.draw(texObjetivo, screenX, screenY, tileSize, tileSize); 
                    break;
                    
                    case CAJA:
                        batch.draw(texPiso, screenX, screenY, tileSize, tileSize);
                        batch.draw(texCaja, screenX, screenY, tileSize, tileSize);
                        break;
                }
            }
        }
    }

    @Override
    public String getNombre(){ 
        return "Nivel 1"; 
    }

    @Override
    public int getNivel(){
        return 1;
    }

    public void dispose(){
        texMuro.dispose();
        texPiso.dispose();
        texObjetivo.dispose();
        texCaja.dispose();
    }
}