/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.Sokoban.filehandling.FileManager;
import com.Sokoban.model.AuthManager;
import com.Sokoban.model.Player;
/**
 *
 * @author Rogelio
 */

public class AvatarScreen implements Screen{

    private final Main game;
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private Texture[] texAvatares;
    private Texture texSeleccionado;
    private String avatarSeleccionado;
    private static final String[] AVATARES = {"a1", "a2", "a3", "a4", "a5"};
    private static final int AV_SIZE = 120;
    private static final int COLS = 3;

    private final boolean desdeRegistro;

    public AvatarScreen(Main game, boolean desdeRegistro){
        this.game = game;
        this.desdeRegistro = desdeRegistro;
    }
    
    @Override
    public void show(){
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        batch = new SpriteBatch();

        Player player = AuthManager.getCurrentPlayer();
        avatarSeleccionado = (player != null) ? player.getAvatarPath() : "aP";

        texAvatares = new Texture[AVATARES.length];
        for(int i = 0; i < AVATARES.length; i++){
            texAvatares[i] = new Texture(Gdx.files.internal("avatars/" + AVATARES[i] + ".png"));
        }
        actualizarTexSeleccionado();

        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(skin.newDrawable("white", new Color(0.118f, 0.118f, 0.180f, 1f)));
        stage.addActor(root);

        Label lblTitle = new Label("SELECCIONAR AVATAR", skin);
        lblTitle.setColor(new Color(0.537f, 0.863f, 0.922f, 1f));
        lblTitle.setFontScale(1.6f);

        Table grid = new Table();
        grid.pad(20);

        for(int i = 0; i < AVATARES.length; i++){
            final String nombre = AVATARES[i];
            final int idx = i;
            TextButton btn = new TextButton(nombre, skin);
            btn.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeEvent event, Actor actor){
                    avatarSeleccionado = nombre;
                    actualizarTexSeleccionado();
                }
            });
            grid.add(btn).width(AV_SIZE).height(AV_SIZE).pad(10);
            if((i + 1) % COLS == 0) grid.row();
        }

        ScrollPane scroll = new ScrollPane(grid, skin);
        scroll.setScrollingDisabled(true, false);
        scroll.setFlickScroll(true);

        TextButton btnConfirmar = new TextButton("Confirmar", skin);
        TextButton btnVolver = new TextButton("Volver", skin);

        root.add(lblTitle).colspan(2).center().padTop(20).padBottom(16).row();
        root.add(scroll).colspan(2).width(500).height(400).row();
        TextButton btnQuitar = new TextButton("Quitar Avatar", skin);
        root.add(btnConfirmar).width(200).height(48).pad(10);
        root.add(btnVolver).width(200).height(48).pad(10).row();
        root.add(btnQuitar).colspan(2).width(200).height(48).pad(10).row();

        btnQuitar.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                Player player = AuthManager.getCurrentPlayer();
                if(player != null){
                    player.setAvatarPath("aP");
                    FileManager.savePlayer(player);
                }
                if(desdeRegistro){
                    game.setScreen(new LoginScreen(game));
                }else{
                    game.setScreen(new ProfileScreen(game));
                }
                dispose();
            }
        });

        btnConfirmar.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                Player player = AuthManager.getCurrentPlayer();
                if(player != null){
                    player.setAvatarPath(avatarSeleccionado);
                    FileManager.savePlayer(player);
                }
                game.setScreen(new ProfileScreen(game));
                dispose();
            }
        });

        btnVolver.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                if(desdeRegistro){
                    game.setScreen(new LoginScreen(game));
                }else{
                    game.setScreen(new ProfileScreen(game));
                }
                dispose();
            }
        });
    }

    private void actualizarTexSeleccionado(){
        for(int i = 0; i < AVATARES.length; i++){
            if(AVATARES[i].equals(avatarSeleccionado)){
                texSeleccionado = texAvatares[i];
                return;
            }
        }
        texSeleccionado = new Texture(Gdx.files.internal("avatars/aP.png"));
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0.118f, 0.118f, 0.180f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        batch.begin();
        batch.draw(texSeleccionado, Gdx.graphics.getWidth() - 160, Gdx.graphics.getHeight() - 160, 128, 128);
        batch.end();
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
        for(Texture t : texAvatares){
            t.dispose();
        }
    }
}