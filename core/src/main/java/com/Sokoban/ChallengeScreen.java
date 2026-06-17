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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.List;

/**
 *
 * @author gpopo
 */
public class ChallengeScreen extends BaseScreen {

    private static final Color BG   = new Color(0.118f, 0.118f, 0.180f, 1f);
    private static final Color CYAN = new Color(0.537f, 0.863f, 0.922f, 1f);
    private static final Color GOLD = new Color(0.976f, 0.886f, 0.686f, 1f);
    private static final Color MUTED = new Color(0.651f, 0.678f, 0.784f, 1f);
    private static final Color GREEN = new Color(0.651f, 0.890f, 0.631f, 1f);
    private static final Color RED   = new Color(0.953f, 0.545f, 0.659f, 1f);

    private final Main game;
    private Stage stage;
    private Skin skin;

    public ChallengeScreen(Main game) {
        this.game = game;
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
        root.top().pad(30);
        root.setBackground(skin.newDrawable("white", BG));
        stage.addActor(root);

        Label lblTitle = new Label(LanguageManager.get("challenges"), skin);
        lblTitle.setColor(CYAN);
        lblTitle.setFontScale(1.8f);
        root.add(lblTitle).center().padBottom(24).row();

        // Retos recibidos pendientes
        Label lblRecibidos = new Label(LanguageManager.get("inbox"), skin);
        lblRecibidos.setColor(MUTED);
        lblRecibidos.setFontScale(1.1f);
        root.add(lblRecibidos).left().padBottom(8).row();

        Table tblRecibidos = new Table();
        List<long[]> recibidos = ChallengeFileManager.getRetosParaUsuario(me.getUsername());

        if (recibidos.isEmpty()) {
            Label lbl = new Label(LanguageManager.get("no_challenges"), skin);
            lbl.setColor(Color.WHITE);
            tblRecibidos.add(lbl).center().padBottom(8).row();
        } else {
            for (long[] reto : recibidos) {
                final long idx  = reto[0];
                final int nivel = (int) reto[1];
                final int scoreRetador = (int) reto[2];

                String retador = ChallengeFileManager.getRetador(idx);

                Label lblInfo = new Label(
                    LanguageManager.get("challenge_from") + " " + retador +
                    "  |  " + LanguageManager.get("level") + " " + nivel +
                    "  |  " + scoreRetador + " pts", skin);
                lblInfo.setColor(GOLD);

                TextButton btnAceptar = new TextButton(LanguageManager.get("accept"), skin);
                btnAceptar.setColor(GREEN);
                TextButton btnRechazar = new TextButton(LanguageManager.get("reject"), skin);
                btnRechazar.setColor(RED);

                btnAceptar.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        ChallengeFileManager.actualizarEstado(idx, ChallengeFileManager.ACEPTADO);
                        game.setScreen(new ChallengeGameScreen(game, nivel, idx, false));
                        dispose();
                    }
                });

                btnRechazar.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        ChallengeFileManager.actualizarEstado(idx, ChallengeFileManager.RECHAZADO);
                        game.setScreen(new ChallengeScreen(game));
                        dispose();
                    }
                });

                tblRecibidos.add(lblInfo).left().width(280).padBottom(8);
                tblRecibidos.add(btnAceptar).width(80).height(36).padRight(6).padBottom(8);
                tblRecibidos.add(btnRechazar).width(80).height(36).padBottom(8).row();
            }
        }

        ScrollPane scrollRecibidos = new ScrollPane(tblRecibidos, skin);
        scrollRecibidos.setScrollingDisabled(true, false);
        root.add(scrollRecibidos).width(500).height(180).padBottom(20).row();

        Label lblEnviados = new Label(LanguageManager.get("challenges_sent"), skin);
        lblEnviados.setColor(MUTED);
        lblEnviados.setFontScale(1.1f);
        root.add(lblEnviados).left().padBottom(8).row();

        Table tblEnviados = new Table();
        List<long[]> enviados = ChallengeFileManager.getRetosEnviados(me.getUsername());

        if (enviados.isEmpty()) {
            Label lbl = new Label(LanguageManager.get("no_challenges"), skin);
            lbl.setColor(Color.WHITE);
            tblEnviados.add(lbl).center().padBottom(8).row();
        } else {
            for (long[] reto : enviados) {
                final long idx  = reto[0];
                final int nivel = (int) reto[1];
                final int scoreRetador = (int) reto[2];
                final int scoreRetado  = (int) reto[3];
                final int estado       = (int) reto[4];

                String retado = ChallengeFileManager.getRetado(idx);
                String estadoStr;
                Color estadoColor;

                switch (estado) {
                    case ChallengeFileManager.PENDIENTE:
                        estadoStr  = "Pendiente";
                        estadoColor = GOLD;
                        break;
                    case ChallengeFileManager.ACEPTADO:
                        estadoStr  = "En juego";
                        estadoColor = CYAN;
                        break;
                    case ChallengeFileManager.COMPLETADO:
                        if (scoreRetador > scoreRetado) {
                            estadoStr  = "Ganaste!";
                            estadoColor = GREEN;
                        } else if (scoreRetador < scoreRetado) {
                            estadoStr  = "Perdiste";
                            estadoColor = RED;
                        } else {
                            estadoStr  = "Empate";
                            estadoColor = GOLD;
                        }
                        break;
                    case ChallengeFileManager.RECHAZADO:
                        estadoStr  = "Rechazado";
                        estadoColor = RED;
                        break;
                    default:
                        estadoStr  = "?";
                        estadoColor = Color.WHITE;
                }

                Label lblInfo = new Label(
                    retado + "  |  " + LanguageManager.get("level") + " " + nivel +
                    "  |  Tu: " + scoreRetador + " pts", skin);
                lblInfo.setColor(Color.WHITE);

                Label lblEstado = new Label(estadoStr, skin);
                lblEstado.setColor(estadoColor);

                tblEnviados.add(lblInfo).left().width(320).padBottom(8);
                tblEnviados.add(lblEstado).width(100).center().padBottom(8).row();
            }
        }

        ScrollPane scrollEnviados = new ScrollPane(tblEnviados, skin);
        scrollEnviados.setScrollingDisabled(true, false);
        root.add(scrollEnviados).width(500).height(180).padBottom(20).row();

        TextButton btnBack = new TextButton(LanguageManager.get("back"), skin);
        root.add(btnBack).width(280).height(48).row();
        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });
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
