/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban;

import com.Sokoban.filehandling.RankingFileManager;
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
public class RankingScreen extends BaseScreen {

    private static final Color BG       = new Color(0.118f, 0.118f, 0.180f, 1f);
    private static final Color CYAN     = new Color(0.537f, 0.863f, 0.922f, 1f);
    private static final Color GOLD     = new Color(0.976f, 0.886f, 0.686f, 1f);
    private static final Color MUTED    = new Color(0.651f, 0.678f, 0.784f, 1f);
    private static final Color GREEN    = new Color(0.651f, 0.890f, 0.631f, 1f);

    private final Main game;
    private Stage stage;
    private Skin skin;

    public RankingScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        iniciarBase();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table root = new Table();
        root.setFillParent(true);
        root.top().pad(30);
        root.setBackground(skin.newDrawable("white", BG));
        stage.addActor(root);

        Label lblTitle = new Label(LanguageManager.get("ranking"), skin);
        lblTitle.setColor(CYAN);
        lblTitle.setFontScale(2f);
        root.add(lblTitle).center().padBottom(24).colspan(3).row();

        // Encabezados
        Label lblPos   = makeHeader("#");
        Label lblUser  = makeHeader(LanguageManager.get("username"));
        Label lblScore = makeHeader(LanguageManager.get("score"));

        Table tblHeader = new Table();
        tblHeader.add(lblPos).width(50).padRight(16);
        tblHeader.add(lblUser).width(200).padRight(16);
        tblHeader.add(lblScore).width(100);
        root.add(tblHeader).colspan(3).padBottom(8).row();

        // Separador
        Label sep = new Label("─────────────────────────────────", skin);
        sep.setColor(MUTED);
        root.add(sep).colspan(3).padBottom(12).row();

        // Lista de ranking
        Table tblRanking = new Table();
        List<String[]> top = RankingFileManager.obtenerTopGlobal(20);

        Player currentPlayer = AuthManager.getCurrentPlayer();
        String currentUsername = currentPlayer != null ? currentPlayer.getUsername() : "";

        if (top.isEmpty()) {
            Label lblVacio = new Label("No hay registros aun.", skin);
            lblVacio.setColor(MUTED);
            tblRanking.add(lblVacio).center().padTop(20).row();
        } else {
            for (String[] entry : top) {
                String pos      = entry[0];
                String username = entry[1];
                String score    = entry[2];

                boolean esMiPuesto = username.equals(currentUsername);

                Label lblP = new Label(pos + ".", skin);
                Label lblU = new Label(username, skin);
                Label lblS = new Label(score + " pts", skin);

                // Resaltar al jugador actual
                if (esMiPuesto) {
                    lblP.setColor(GREEN);
                    lblU.setColor(GREEN);
                    lblS.setColor(GREEN);
                } else if (pos.equals("1")) {
                    lblP.setColor(GOLD);
                    lblU.setColor(GOLD);
                    lblS.setColor(GOLD);
                } else {
                    lblP.setColor(Color.WHITE);
                    lblU.setColor(Color.WHITE);
                    lblS.setColor(Color.WHITE);
                }

                tblRanking.add(lblP).width(50).padRight(16).padBottom(10);
                tblRanking.add(lblU).width(200).padRight(16).padBottom(10);
                tblRanking.add(lblS).width(100).padBottom(10).row();
            }
        }

        ScrollPane scroll = new ScrollPane(tblRanking, skin);
        scroll.setScrollingDisabled(true, false);
        scroll.setFlickScroll(true);
        root.add(scroll).colspan(3).width(420).height(380).padBottom(20).row();

        // Mi puntaje
        if (currentPlayer != null) {
            Label lblMiPuntaje = new Label(
                LanguageManager.get("username") + ": " + currentPlayer.getUsername() +
                "  |  " + currentPlayer.getTotalScore() + " pts", skin);
            lblMiPuntaje.setColor(GREEN);
            root.add(lblMiPuntaje).colspan(3).center().padBottom(16).row();
        }

        TextButton btnBack = new TextButton(LanguageManager.get("back"), skin);
        root.add(btnBack).colspan(3).width(280).height(48).row();
        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });
    }

    private Label makeHeader(String text) {
        Label lbl = new Label(text, skin);
        lbl.setColor(CYAN);
        lbl.setFontScale(1.1f);
        return lbl;
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
