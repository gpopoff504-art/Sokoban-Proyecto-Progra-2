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
import com.Sokoban.model.Player;

/**
 *
 * @author gpopo
 */
public class ProfileScreen implements Screen {

    private final Main game;
    private Stage stage;
    private Skin skin;

    public ProfileScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Player player = AuthManager.getCurrentPlayer();

        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(skin.newDrawable("white", new Color(0.118f, 0.118f, 0.180f, 1f)));
        stage.addActor(root);

        Label lblTitle = new Label("MI PERFIL", skin);
        lblTitle.setColor(new Color(0.537f, 0.863f, 0.922f, 1f));
        lblTitle.setFontScale(1.8f);

        root.pad(40);
        root.add(lblTitle).center().padBottom(28).row();

        if (player != null) {
            addRow(root, skin, "Usuario",       player.getUsername());
            addRow(root, skin, "Nombre",        player.getFullName());
            addRow(root, skin, "Nivel actual",  String.valueOf(player.getCurrentLevel()));
            addRow(root, skin, "Puntuacion",    String.valueOf(player.getTotalScore()));
            addRow(root, skin, "Registrado",    player.getRegistrationDate().toLocalDate().toString());
        }

        TextButton btnBack = new TextButton("Volver al Menu", skin);
        root.add(btnBack).width(280).height(48).padTop(28).row();

        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });
    }

    private void addRow(Table table, Skin skin, String key, String value) {
        Label lKey = new Label(key + ":", skin);
        lKey.setColor(new Color(0.804f, 0.831f, 0.957f, 1f));

        Label lVal = new Label(value, skin);
        lVal.setColor(Color.WHITE);

        table.add(lKey).left().padRight(16).padBottom(12);
        table.add(lVal).left().padBottom(12).row();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.118f, 0.118f, 0.180f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
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
        stage.dispose();
        skin.dispose();
    }
}
