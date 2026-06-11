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

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0.118f, 0.118f, 0.180f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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