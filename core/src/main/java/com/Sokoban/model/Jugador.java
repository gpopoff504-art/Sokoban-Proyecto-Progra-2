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

public class Jugador{

    private int gridX;
    private int gridY;
    private Texture texActual;
    private Texture idle;
    private Texture left1, left2, leftPush1, leftPush2;
    private Texture right1, right2, rightPush1, rightPush2;
    private Texture up1, up2, upPush1, upPush2;
    private Texture down1, down2, downPush1, downPush2;
    private float animTimer = 0;
    private int animFrame = 0;
    private static final float FRAME_TIME = 0.2f;
    private String dirActual = null;
    private boolean empujandoActual = false;
    private float idleTimer = 0;
    private static final float IDLE_DELAY = 0.3f;

    public Jugador(int gridX, int gridY){
        this.gridX = gridX;
        this.gridY = gridY;
        cargarSprites();
        texActual = idle;
    }

    private void cargarSprites(){
        idle = new Texture(Gdx.files.internal("sprites/PersonajeSprites/Pidle.png"));
        left1 = new Texture(Gdx.files.internal("sprites/PersonajeSprites/Pleft1.png"));
        left2 = new Texture(Gdx.files.internal("sprites/PersonajeSprites/Pleft2.png"));
        leftPush1 = new Texture(Gdx.files.internal("sprites/PersonajeSprites/PleftPush1.png"));
        leftPush2 = new Texture(Gdx.files.internal("sprites/PersonajeSprites/PleftPush2.png"));
        right1 = new Texture(Gdx.files.internal("sprites/PersonajeSprites/Pright1.png"));
        right2 = new Texture(Gdx.files.internal("sprites/PersonajeSprites/Pright2.png"));
        rightPush1 = new Texture(Gdx.files.internal("sprites/PersonajeSprites/PrightPush1.png"));
        rightPush2 = new Texture(Gdx.files.internal("sprites/PersonajeSprites/PrightPush2.png"));
        up1 = new Texture(Gdx.files.internal("sprites/PersonajeSprites/Pup1.png"));
        up2 = new Texture(Gdx.files.internal("sprites/PersonajeSprites/Pup2.png"));
        upPush1 = new Texture(Gdx.files.internal("sprites/PersonajeSprites/PupPush1.png"));
        upPush2 = new Texture(Gdx.files.internal("sprites/PersonajeSprites/PupPush2.png"));
        down1 = new Texture(Gdx.files.internal("sprites/PersonajeSprites/Pdown1.png"));
        down2 = new Texture(Gdx.files.internal("sprites/PersonajeSprites/Pdown2.png"));
        downPush1 = new Texture(Gdx.files.internal("sprites/PersonajeSprites/PdownPush1.png"));
        downPush2 = new Texture(Gdx.files.internal("sprites/PersonajeSprites/PdownPush2.png"));
    }

    public void setIdle(){
        dirActual = null;
        empujandoActual = false;
        texActual = idle;
        animFrame = 0;
        animTimer = 0;
    }

    public void setMoviendo(String direccion, boolean empujando){
        this.dirActual = direccion;
        this.empujandoActual = empujando;
        this.idleTimer = 0;
        this.animFrame = 0;
        actualizarTextura();      
    }

    public void render(SpriteBatch batch, int tileSize, int offsetX, int offsetY, int filas){
        int drawW = 50;
        int drawH = 60;
        int screenX = offsetX + gridX * tileSize + (tileSize - drawW) / 2;
        int screenY = offsetY + (filas - 1 - gridY) * tileSize + (tileSize - drawH) / 2;
        batch.draw(texActual, screenX, screenY, drawW, drawH);
    }

    public int getGridX(){ 
        return gridX; 
    }
    public int getGridY(){ 
        return gridY; 
    }
    public void setGridX(int x){ 
        this.gridX = x;
    }
    public void setGridY(int y){
        this.gridY = y; 
    }

    public void dispose(){
        idle.dispose();
        left1.dispose(); left2.dispose(); leftPush1.dispose(); leftPush2.dispose();
        right1.dispose(); right2.dispose(); rightPush1.dispose(); rightPush2.dispose();
        up1.dispose(); up2.dispose(); upPush1.dispose(); upPush2.dispose();
        down1.dispose(); down2.dispose(); downPush1.dispose(); downPush2.dispose();
    }
    
    public void update(float delta) {
        if (dirActual != null) {
            idleTimer += delta;
            animTimer += delta;
            if(animTimer >= FRAME_TIME){
                animTimer = 0;
                animFrame = (animFrame + 1) % 2;
                actualizarTextura();
            }
            if(idleTimer >= IDLE_DELAY) {
                setIdle();
            }
        }
    }
    
    private void actualizarTextura() {
        if(dirActual == null) return;
        switch (dirActual) {
            case "left": texActual = empujandoActual ? (animFrame == 0 ? leftPush1 : leftPush2) : (animFrame == 0 ? left1 : left2); break;
            case "right": texActual = empujandoActual ? (animFrame == 0 ? rightPush1 : rightPush2) : (animFrame == 0 ? right1 : right2); break;
            case "up":    texActual = empujandoActual ? (animFrame == 0 ? upPush1    : upPush2)    : (animFrame == 0 ? up1    : up2);    break;
            case "down":  texActual = empujandoActual ? (animFrame == 0 ? downPush1  : downPush2)  : (animFrame == 0 ? down1  : down2);  break;        
        }
    }
}