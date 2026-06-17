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

/**
 *
 * @author gpopo
 */
public class MenuScreen extends BaseScreen {

    private final Main game;
    private Stage stage;
    private Skin skin;

    public MenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        iniciarBase();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Player player = AuthManager.getCurrentPlayer();
        
        if (player != null) {
            LanguageManager.initFromPlayer(player);
        }

        String name   = (player != null) ? player.getFullName() : "Jugador";
        int level     = (player != null) ? player.getCurrentLevel() : 1;
        int score     = (player != null) ? player.getTotalScore()   : 0;

        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(skin.newDrawable("white", new Color(0.118f, 0.118f, 0.180f, 1f)));
        stage.addActor(root);

        Label lblTitle = new Label("SOKOBAN", skin);
        lblTitle.setColor(new Color(0.537f, 0.863f, 0.922f, 1f));
        lblTitle.setFontScale(2.4f);

        Label lblWelcome = new Label(LanguageManager.get("welcome") + " " + name + "!", skin);
        lblWelcome.setColor(new Color(0.651f, 0.678f, 0.784f, 1f));

        Label lblStats = new Label(LanguageManager.get("level") + ": " + level + "   |   " + score + " pts", skin);
        lblStats.setColor(new Color(0.976f, 0.886f, 0.686f, 1f));

        TextButton btnPlay = new TextButton(LanguageManager.get("play"), skin); 
        TextButton btnProfile  = new TextButton(LanguageManager.get("profile"), skin);
        TextButton btnFriends = new TextButton(LanguageManager.get("friends"), skin);
        TextButton btnSettings = new TextButton(LanguageManager.get("settings"), skin);
        TextButton btnLogout   = new TextButton(LanguageManager.get("logout"), skin);

        root.pad(40);
        root.add(lblTitle).center().padBottom(8).row();
        root.add(lblWelcome).center().padBottom(4).row();
        root.add(lblStats).center().padBottom(36).row();
        root.add(btnPlay).width(280).height(52).padBottom(12).row();
        root.add(btnProfile).width(280).height(52).padBottom(12).row();
        root.add(btnFriends).width(280).height(52).padBottom(12).row();
        root.add(btnSettings).width(280).height(52).padBottom(12).row();
        root.add(btnLogout).width(280).height(52).row();

        btnPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Screen oldScreen = game.getScreen();
                game.setScreen(new MapScreen(game));
                if (oldScreen != null) oldScreen.dispose();
            }
        });

        btnProfile.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Screen oldScreen = game.getScreen();
                game.setScreen(new ProfileScreen(game));
                if (oldScreen != null) oldScreen.dispose();
            }
        });

        btnFriends.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                game.setScreen(new FriendsScreen(game));
                dispose();
            }
        });

        btnSettings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Screen oldScreen = game.getScreen();
                game.setScreen(new SettingsScreen(game));
                if (oldScreen != null) oldScreen.dispose();
            }
        });

        btnLogout.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Screen oldScreen = game.getScreen();
                game.setScreen(new LoginScreen(game));
                if (oldScreen != null) oldScreen.dispose();
            }         
        });
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
    }
}
