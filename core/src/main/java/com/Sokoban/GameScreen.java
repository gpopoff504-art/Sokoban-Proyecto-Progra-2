/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.Sokoban.model.Jugador;
import com.Sokoban.model.Level1;
import com.Sokoban.model.Level;

/**
 *
 * @author gpopo
 */
public class GameScreen implements Screen{

    private final Main game;
    private final int level;
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private Level1 nivel1;
    private Jugador jugador;
    private static final int TILE_SIZE = 80;
    private int offsetX;
    private int offsetY;

    public GameScreen(Main game, int level){
        this.game = game;
        this.level = level;
    }

    @Override
    public void show(){
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        batch = new SpriteBatch();
        nivel1 = new Level1();
        offsetX = (Gdx.graphics.getWidth() - nivel1.getColumnas() * TILE_SIZE) / 2;
        offsetY = (Gdx.graphics.getHeight() - nivel1.getFilas() * TILE_SIZE) / 2;
        jugador = new Jugador(nivel1.getJugadorX(), nivel1.getJugadorY());

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        TextButton btnBack = new TextButton("Volver al Mapa", skin);
        root.bottom().pad(10);
        root.add(btnBack).width(240).height(48);

        btnBack.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                game.setScreen(new MapScreen(game));
                dispose();
            }
        });
    }
    
    private void handleInput() {
        int dx = 0, dy = 0;
        String dir = null;
        
        if(Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.LEFT) || 
                Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.A)) {
            dx = -1; dir = "left";
        }
        else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.RIGHT) || 
                Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.D)) {
            dx = 1; dir = "right";
        }
        else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.UP) || 
                Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.W)) {
            dy = -1; dir = "up";
        }
        else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.DOWN) || 
                Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.S)){
            dy = 1; dir = "down";
        }
        
        if (dir != null) {
            moverJugador(dx, dy, dir);
        }
    }
    
    private void moverJugador(int dx, int dy, String dir){
        int [][] mapa = nivel1.getMapa();
        int px = jugador.getGridX();
        int py = jugador.getGridY();
        
        int nx = px + dx;
        int ny = py + dy;
        
        if (nx < 0 || ny < 0 || ny >= nivel1.getFilas() || nx >= nivel1.getColumnas()) return;
        
        int celda = mapa[ny][nx];
        
        if (celda == Level.MURO || celda == Level.VACIO) {
            jugador.setIdle();
            return;
        }
        
        if(celda == Level.CAJA) {
            int cx = nx + dx;
            int cy = ny + dy;
            
            if(cx < 0 || cy < 0 || cy >= nivel1.getFilas() || cx >= nivel1.getColumnas()) return;
            
            int detras = mapa [cy][cx];
            if(detras == Level.MURO || detras == Level.CAJA || detras == Level.VACIO) {
                jugador.setIdle();
                return;
            }
            
            mapa[cy][cx] = Level.CAJA;
            mapa[ny][nx] = Level.PISO;
            jugador.setMoviendo(dir, true);
        }else {
            jugador.setMoviendo(dir, false);
        }
        jugador.setGridX(nx);
        jugador.setGridY(ny);
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0.118f, 0.118f, 0.180f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        handleInput();
        jugador.update(delta);
        batch.begin();
        nivel1.render(batch, TILE_SIZE, offsetX, offsetY);
        jugador.render(batch, TILE_SIZE, offsetX, offsetY, nivel1.getFilas());
        batch.end();
        
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height){
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause(){}
    @Override public void resume(){}
    @Override public void hide(){}

    @Override
    public void dispose(){
        stage.dispose();
        skin.dispose();
        batch.dispose();
        nivel1.dispose();
        jugador.dispose();
    }
    

}