/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.Sokoban.model.AuthManager;
import com.Sokoban.model.LanguageManager;
import com.Sokoban.model.Player;
import com.badlogic.gdx.graphics.Texture;

/**
 *
 * @author gpopo
 */
public class ProfileScreen extends BaseScreen {

    private final Main game;
    private Stage stage;
    private Skin skin;
    private Texture texAvatar;
    
    public ProfileScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        iniciarBase();
        Player player = AuthManager.getCurrentPlayer();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        
        String rutaAvatar = (player != null && !player.getAvatarPath().equals("default")) 
            ? "avatars/" + player.getAvatarPath() + ".png" 
            : "avatars/aP.png";
        texAvatar = new Texture(Gdx.files.internal(rutaAvatar));

        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(skin.newDrawable("white", new Color(0.118f, 0.118f, 0.180f, 1f)));
        stage.addActor(root);

        Label lblTitle = new Label(LanguageManager.get(LanguageManager.KEY_PROFILE), skin);
        lblTitle.setColor(new Color(0.537f, 0.863f, 0.922f, 1f));
        lblTitle.setFontScale(1.8f);
        root.center().pad(40);   
        root.add(lblTitle).center().padBottom(28).colspan(2).row();

        if (player != null) {
            addRow(root, skin, LanguageManager.get(LanguageManager.KEY_USERNAME), player.getUsername());
            addRow(root, skin, "Nombre", player.getFullName());
            addRow(root, skin, LanguageManager.get(LanguageManager.KEY_LEVEL), String.valueOf(player.getCurrentLevel()));
            addRow(root, skin, "Puntuación", String.valueOf(player.getTotalScore()));
            addRow(root, skin, "Registrado", player.getRegistrationDate().toLocalDate().toString());
            addRow(root, skin, "Última sesión", player.getLastSession().toLocalDate().toString());
            
            long tiempo = player.getTotalTimePlayed();
            long horas = tiempo / 3600;
            long minutos = (tiempo % 3600) / 60;
            long segundos = tiempo % 60;
            String tiempoFormato;
            if (horas > 0) {
                tiempoFormato = horas + "h " + minutos + "m " + segundos + "s";
            } else if (minutos > 0) {
                tiempoFormato = minutos + "m " + segundos + "s";
            } else {
                tiempoFormato = segundos + "s";
            }
            addRow(root, skin, "Tiempo jugado", tiempoFormato);
            addRow(root, skin, "Partidas jugadas", String.valueOf(player.getGameHistory().size()));

            TextButton btnAvatar = new TextButton("Cambiar Avatar", skin);
            root.add(btnAvatar).colspan(2).center().width(280).height(48).padTop(16).row();

            btnAvatar.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Screen oldScreen = game.getScreen();
                    game.setScreen(new AvatarScreen(game, false));
                    if (oldScreen != null) oldScreen.dispose();
                }
            });
        }

        TextButton btnBack = new TextButton(LanguageManager.get(LanguageManager.KEY_BACK), skin);
        root.add(btnBack).colspan(2).center().width(280).height(48).padTop(28).row();
        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Screen oldScreen = game.getScreen();
                game.setScreen(new MenuScreen(game));
                if (oldScreen != null) oldScreen.dispose();
            }
        });
    }

    private void addRow(Table table, Skin skin, String key, String value) {
        Label lKey = new Label(key + ":", skin);
        lKey.setColor(new Color(0.804f, 0.831f, 0.957f, 1f));
        Label lVal = new Label(value, skin);
        lVal.setColor(Color.WHITE);
        table.add(lKey).left().padRight(8).padBottom(12);
        table.add(lVal).left().padBottom(12).row();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.118f, 0.118f, 0.180f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        dibujarHUD();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        disposeBase();
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
        if (texAvatar != null) texAvatar.dispose();
    }
}
