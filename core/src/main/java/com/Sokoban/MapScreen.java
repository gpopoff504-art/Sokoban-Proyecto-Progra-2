/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban;

import com.badlogic.gdx.Gdx;
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
public class MapScreen extends BaseScreen {

    private static final int TOTAL_LEVELS = 5;

    private final Main game;
    private Stage stage;
    private Skin skin;

    public MapScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {    
        iniciarBase();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Player player       = AuthManager.getCurrentPlayer();
        int unlockedLevel   = (player != null) ? player.getCurrentLevel() : 1;

        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(skin.newDrawable("white", new Color(0.118f, 0.118f, 0.180f, 1f)));
        stage.addActor(root);

        Label lblTitle = new Label(LanguageManager.get("select_level"), skin);
        lblTitle.setColor(new Color(0.537f, 0.863f, 0.922f, 1f));
        lblTitle.setFontScale(1.6f);

        Label lblInfo = new Label(LanguageManager.get("unlocked") + " " + unlockedLevel, skin);
        lblInfo.setColor(new Color(0.651f, 0.678f, 0.784f, 1f));

        root.add(lblTitle).colspan(3).center().padTop(30).padBottom(8).row();
        root.add(lblInfo).colspan(3).center().padBottom(24).row();

        Color[] worldColors = {
            new Color(0.651f, 0.890f, 0.631f, 1f),
            new Color(0.537f, 0.706f, 0.980f, 1f),
            new Color(0.976f, 0.886f, 0.686f, 1f)
        };

        for (int i = 1; i <= TOTAL_LEVELS; i++) {
            final int lvl  = i;
            boolean unlock = i <= unlockedLevel;

            TextButton btn = new TextButton(unlock ? String.valueOf(i) : "X", skin);
            btn.setColor(unlock ? worldColors[(i - 1) / 3] : new Color(0.35f, 0.35f, 0.45f, 1f));
            btn.setDisabled(!unlock);

            if (unlock) {
                btn.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        game.setScreen(new GameScreen(game, lvl));
                        dispose();
                    }
                });
            }

            root.add(btn).width(100).height(100).pad(8);
            if (i % 3 == 0) root.row();
        }

        TextButton btnBack = new TextButton(LanguageManager.get("back_menu"), skin);
        root.row();
        root.add(btnBack).colspan(3).width(280).height(48).padTop(24).padBottom(20);

        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });
    }

    @Override
    public void render(float delta){
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
        stage.dispose();
        skin.dispose();
    }
}
