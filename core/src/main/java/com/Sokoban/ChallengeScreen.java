/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban;

import com.Sokoban.filehandling.ChallengeFileManager;
import com.Sokoban.filehandling.FileManager;
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

    private static final Color BG    = new Color(0.118f, 0.118f, 0.180f, 1f);
    private static final Color CYAN  = new Color(0.537f, 0.863f, 0.922f, 1f);
    private static final Color MUTED = new Color(0.651f, 0.678f, 0.784f, 1f);
    private static final Color GREEN = new Color(0.651f, 0.890f, 0.631f, 1f);
    private static final Color RED   = new Color(0.953f, 0.545f, 0.659f, 1f);
    private static final Color GOLD  = new Color(0.976f, 0.886f, 0.686f, 1f);

    private final Main game;
    private Stage stage;
    private Skin skin;
    private Label lblMsg;

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
        root.top().pad(28);
        root.setBackground(skin.newDrawable("white", BG));
        stage.addActor(root);

        Label lblTitle = new Label(LanguageManager.get("challenges"), skin);
        lblTitle.setColor(CYAN);
        lblTitle.setFontScale(1.8f);
        root.add(lblTitle).colspan(2).center().padBottom(6).row();

        lblMsg = new Label("", skin);
        lblMsg.setColor(GREEN);
        root.add(lblMsg).colspan(2).center().padBottom(10).row();

        // Retos recibidos pendientes
        Label lblRecibidos = new Label(LanguageManager.get("challenges_received"), skin);
        lblRecibidos.setColor(MUTED);
        root.add(lblRecibidos).colspan(2).left().padBottom(4).row();

        Table tblRecibidos = new Table();
        List<long[]> recibidos = ChallengeFileManager.getRetosParaUsuario(me.getUsername());
        if (recibidos.isEmpty()) {
            Label lbl = new Label(LanguageManager.get("no_challenges"), skin);
            lbl.setColor(Color.WHITE);
            tblRecibidos.add(lbl).row();
        } else {
            for (long[] rec : recibidos) {
                final long idx   = rec[0];
                final int  nivel = (int) rec[1];
                final int  sRet  = (int) rec[2];
                String retador = ChallengeFileManager.getRetador(idx);

                String info = retador + "  →  " + LanguageManager.get("level") + " " + nivel
                    + "   " + LanguageManager.get("their_score") + ": " + (sRet > 0 ? sRet + " pts" : "?");
                Label lblInfo = new Label(info, skin);
                lblInfo.setColor(Color.WHITE);

                TextButton btnAceptar  = new TextButton(LanguageManager.get("accept"), skin);
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

                tblRecibidos.add(lblInfo).left().width(260).padBottom(8);
                tblRecibidos.add(btnAceptar).width(80).height(34).padRight(4).padBottom(8);
                tblRecibidos.add(btnRechazar).width(80).height(34).padBottom(8).row();
            }
        }

        ScrollPane scrollRec = new ScrollPane(tblRecibidos, skin);
        scrollRec.setScrollingDisabled(true, false);
        root.add(scrollRec).colspan(2).width(480).height(130).padBottom(14).row();

        // Retos enviados / en curso
        Label lblEnviados = new Label(LanguageManager.get("challenges_sent"), skin);
        lblEnviados.setColor(MUTED);
        root.add(lblEnviados).colspan(2).left().padBottom(4).row();

        Table tblEnviados = new Table();
        List<long[]> enviados = ChallengeFileManager.getRetosEnviados(me.getUsername());
        if (enviados.isEmpty()) {
            Label lbl = new Label(LanguageManager.get("no_challenges"), skin);
            lbl.setColor(Color.WHITE);
            tblEnviados.add(lbl).row();
        } else {
            for (long[] env : enviados) {
                final long idx   = env[0];
                final int  nivel = (int) env[1];
                final int  sRet  = (int) env[2];
                final int  sRtd  = (int) env[3];
                final int  estado = (int) env[4];
                String retado = ChallengeFileManager.getRetado(idx);

                String estadoStr;
                Color  estadoColor;
                switch (estado) {
                    case ChallengeFileManager.PENDIENTE:
                        estadoStr = LanguageManager.get("challenge_pending"); estadoColor = GOLD; break;
                    case ChallengeFileManager.ACEPTADO:
                        estadoStr = LanguageManager.get("challenge_playing"); estadoColor = CYAN; break;
                    case ChallengeFileManager.COMPLETADO:
                        boolean gane = sRet > sRtd;
                        estadoStr = gane ? LanguageManager.get("challenge_won")
                                        : LanguageManager.get("challenge_lost");
                        estadoColor = gane ? GREEN : RED;
                        break;
                    default:
                        estadoStr = LanguageManager.get("challenge_rejected"); estadoColor = RED; break;
                }

                String info = retado + "  ·  " + LanguageManager.get("level") + " " + nivel
                    + "   " + LanguageManager.get("score") + ": " + sRet + " pts";
                Label lblInfo = new Label(info, skin);
                lblInfo.setColor(Color.WHITE);
                Label lblEstado = new Label(estadoStr, skin);
                lblEstado.setColor(estadoColor);

                tblEnviados.add(lblInfo).left().width(300).padBottom(8);
                tblEnviados.add(lblEstado).left().width(160).padBottom(8).row();

                // Si completado, mostrar resultado detallado
                if (estado == ChallengeFileManager.COMPLETADO) {
                    Label lblScores = new Label(
                        "  " + me.getUsername() + ": " + sRet + " pts   " + retado + ": " + sRtd + " pts", skin);
                    lblScores.setColor(MUTED);
                    tblEnviados.add(lblScores).colspan(2).left().padBottom(8).row();
                }
            }
        }

        ScrollPane scrollEnv = new ScrollPane(tblEnviados, skin);
        scrollEnv.setScrollingDisabled(true, false);
        root.add(scrollEnv).colspan(2).width(480).height(150).padBottom(14).row();

        TextButton btnBack = new TextButton(LanguageManager.get("back"), skin);
        root.add(btnBack).colspan(2).width(280).height(48).padTop(8).row();
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
        if (skin != null) skin.dispose();
    }
}
