/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban;

import com.Sokoban.filehandling.ChallengeFileManager;
import com.Sokoban.model.AuthManager;
import com.Sokoban.model.LanguageManager;
import com.Sokoban.model.Player;
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

/**
 *
 * @author gpopo
 */
public class SendChallengeScreen extends BaseScreen {

    private static final Color BG   = new Color(0.118f, 0.118f, 0.180f, 1f);
    private static final Color CYAN = new Color(0.537f, 0.863f, 0.922f, 1f);
    private static final Color GOLD = new Color(0.976f, 0.886f, 0.686f, 1f);
    private static final Color RED  = new Color(0.953f, 0.545f, 0.659f, 1f);

    private final Main   game;
    private final String retado;
    private Stage stage;
    private Skin  skin;
    private Label lblMsg;

    public SendChallengeScreen(Main game, String retado) {
        this.game   = game;
        this.retado = retado;
    }

    @Override
    public void show() {
        iniciarBase();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Player me = AuthManager.getCurrentPlayer();

        Table root = new Table();
        root.setFillParent(true);
        root.top().pad(40);
        root.setBackground(skin.newDrawable("white", BG));
        stage.addActor(root);

        Label lblTitle = new Label(LanguageManager.get("challenge") + " " + retado, skin);
        lblTitle.setColor(CYAN);
        lblTitle.setFontScale(1.6f);
        root.add(lblTitle).center().padBottom(8).row();

        Label lblSub = new Label(LanguageManager.get("select_level"), skin);
        lblSub.setColor(new Color(0.651f, 0.678f, 0.784f, 1f));
        root.add(lblSub).center().padBottom(24).row();

        lblMsg = new Label("", skin);
        lblMsg.setColor(GOLD);
        root.add(lblMsg).center().padBottom(12).row();

        int unlockedLevel = (me != null) ? me.getCurrentLevel() : 1;

        Table grid = new Table();
        Color[] colores = {
            new Color(0.651f, 0.890f, 0.631f, 1f),
            new Color(0.537f, 0.706f, 0.980f, 1f),
            new Color(0.976f, 0.886f, 0.686f, 1f)
        };

        for (int i = 1; i <= 5; i++) {
            final int lvl    = i;
            boolean unlocked = i <= unlockedLevel;
            TextButton btn   = new TextButton(unlocked ? String.valueOf(i) : "X", skin);
            btn.setColor(unlocked ? colores[(i - 1) / 3] : new Color(0.35f, 0.35f, 0.45f, 1f));
            btn.setDisabled(!unlocked);

            if (unlocked) {
                btn.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        enviarReto(lvl);
                    }
                });
            }
            grid.add(btn).width(80).height(80).pad(6);
            if (i % 3 == 0) grid.row();
        }
        root.add(grid).padBottom(24).row();

        TextButton btnBack = new TextButton(LanguageManager.get("back"), skin);
        root.add(btnBack).width(240).height(48).row();
        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new FriendsScreen(game));
                dispose();
            }
        });
    }

    private void enviarReto(int nivel) {
        Player me = AuthManager.getCurrentPlayer();
        if (me == null) return;

        // El retador juega primero
        boolean ok = ChallengeFileManager.enviarReto(me.getUsername(), retado, nivel);
        if (!ok) {
            lblMsg.setText(LanguageManager.get("challenge_exists"));
            lblMsg.setColor(RED);
            return;
        }

        // Obtener idx del reto recién creado para jugar inmediatamente
        java.util.List<long[]> enviados = ChallengeFileManager.getRetosEnviados(me.getUsername());
        long idx = -1;
        for (long[] d : enviados) {
            if ((int) d[1] == nivel && (int) d[4] == ChallengeFileManager.PENDIENTE) idx = d[0];
        }

        if (idx >= 0) {
            game.setScreen(new ChallengeGameScreen(game, nivel, idx, true));
            dispose();
        } else {
            lblMsg.setText(LanguageManager.get("error"));
            lblMsg.setColor(RED);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(BG.r, BG.g, BG.b, 1f);
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
        if (skin  != null) skin.dispose();
    }
}
