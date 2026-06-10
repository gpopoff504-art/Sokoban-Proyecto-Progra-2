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
import com.Sokoban.model.Level;
import com.Sokoban.model.Level1;
import com.Sokoban.model.Level2;
import com.Sokoban.model.Level3;
import com.Sokoban.model.Level4;
import com.Sokoban.model.Level5;
import com.Sokoban.model.AuthManager;
import com.Sokoban.model.Player;
import com.Sokoban.filehandling.FileManager;
import com.Sokoban.filehandling.RankingFileManager;
import com.Sokoban.model.LanguageManager;

/**
 *
 * @author gpopo
 */
public class GameScreen implements Screen {

    private final Main game;
    private final int level;
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private Level nivelActual;
    private Jugador jugador;
    private Juego juego; 
    private static final int TILE_SIZE = 80;
    private int offsetX;
    private int offsetY;

    private Table panelVictoria;
    private Label lblTiempo;
    private Label lblMovimientos;
    private Label lblTiempoVictoria;
    private Label lblMovsVictoria;
    private Label lblPuntajeVictoria;

    public GameScreen(Main game, int level) {
        this.game = game;
        this.level = level;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        batch = new SpriteBatch();

        switch (level) {
            case 1: nivelActual = new Level1(); break;
            case 2: nivelActual = new Level2(); break;
            case 3: nivelActual = new Level3(); break;
            case 4: nivelActual = new Level4(); break;
            case 5: nivelActual = new Level5(); break;
            default: nivelActual = new Level1(); break;
        }

        offsetX = (Gdx.graphics.getWidth() - nivelActual.getColumnas() * TILE_SIZE) / 2;
        offsetY = (Gdx.graphics.getHeight() - nivelActual.getFilas() * TILE_SIZE) / 2;
        jugador = new Jugador(nivelActual.getJugadorX(), nivelActual.getJugadorY());
        juego = new Sokoban(nivelActual, jugador);

        // HUD arriba
        Table hud = new Table();
        hud.setFillParent(true);
        hud.top().pad(10);
        stage.addActor(hud);

        lblTiempo = new Label(LanguageManager.get("time") + ": 0s", skin);
        lblTiempo.setColor(new Color(0.976f, 0.886f, 0.686f, 1f));
        hud.add(lblTiempo).expandX().left().padLeft(20);

        Label lblNivel = new Label(LanguageManager.get("level") + ": " + level, skin);
        lblNivel.setColor(Color.WHITE);
        hud.add(lblNivel).expandX().center();

        lblMovimientos = new Label(LanguageManager.get("movements") + ": 0", skin);
        lblMovimientos.setColor(new Color(0.976f, 0.886f, 0.686f, 1f));
        hud.add(lblMovimientos).expandX().right().padRight(20);

        Table rootBottom = new Table();
        rootBottom.setFillParent(true);
        rootBottom.bottom().pad(10);
        stage.addActor(rootBottom);

        TextButton btnBack = new TextButton(LanguageManager.get("back_map"), skin);
        rootBottom.add(btnBack).width(240).height(48);
        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Player player = AuthManager.getCurrentPlayer();
                if (player != null) {
                    player.setTotalTimePlayed(player.getTotalTimePlayed() + (long) juego.getTiempoTotal());
                    FileManager.savePlayer(player);
                }
                game.setScreen(new MapScreen(game));
                dispose();
            }
        });

        TextButton btnReset = new TextButton(LanguageManager.get("reset"), skin);
        rootBottom.add(btnReset).width(140).height(48).padLeft(10);
        btnReset.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Player player = AuthManager.getCurrentPlayer();
                if (player != null) {
                    player.setTotalTimePlayed(player.getTotalTimePlayed() + (long) juego.getTiempoTotal());
                    FileManager.savePlayer(player);
                }
                game.setScreen(new GameScreen(game, level));
                dispose();
            }
        });

        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setVisible(false);
        overlay.setBackground(skin.newDrawable("white", new Color(0f, 0f, 0f, 0.7f)));
        stage.addActor(overlay);
        panelVictoria = overlay;

        Label lblGano = new Label(LanguageManager.get("you_win"), skin);
        lblGano.setColor(new Color(0.537f, 0.863f, 0.922f, 1f));
        lblGano.setFontScale(2f);

        lblTiempoVictoria = new Label(LanguageManager.get("time") + ": 0s", skin);
        lblTiempoVictoria.setColor(Color.WHITE);

        lblMovsVictoria = new Label(LanguageManager.get("movements") + ": 0", skin);
        lblMovsVictoria.setColor(Color.WHITE);

        lblPuntajeVictoria = new Label("", skin);
        lblPuntajeVictoria.setColor(new Color(0.976f, 0.886f, 0.686f, 1f));
        lblPuntajeVictoria.setFontScale(1.3f);

        TextButton btnSiguiente = new TextButton(
            level == 5 ? LanguageManager.get("back_menu") : LanguageManager.get("next_level"), skin);

        overlay.center();
        overlay.add(lblGano).padBottom(16).row();
        overlay.add(lblTiempoVictoria).padBottom(8).row();
        overlay.add(lblMovsVictoria).padBottom(8).row();
        overlay.add(lblPuntajeVictoria).padBottom(24).row();
        overlay.add(btnSiguiente).width(240).height(48).row();

        btnSiguiente.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (level == 5) {
                    game.setScreen(new MenuScreen(game));
                } else {
                    game.setScreen(new GameScreen(game, level + 1));
                }
                dispose();
            }
        });
    }

    private void handleInput() {
        if (juego.isGano()) return;
        String dir = null;

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.LEFT) ||
                Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.A)) {
            dir = "left";
        } else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.RIGHT) ||
                Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.D)) {
            dir = "right";
        } else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.UP) ||
                Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.W)) {
            dir = "up";
        } else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.DOWN) ||
                Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.S)) {
            dir = "down";
        }

        if (dir != null) {
            juego.mover(dir);
        }
    }

    private void procesarVictoria() {
        float tiempoTotal = juego.getTiempoTotal();
        int movimientos = juego.getMovimientos();

        int seg = (int) tiempoTotal;
        int centesimas = (int) ((tiempoTotal - seg) * 100);
        lblTiempoVictoria.setText(LanguageManager.get("time") + ": " + seg + "." + String.format("%02d", centesimas) + "s");
        lblMovsVictoria.setText(LanguageManager.get("movements") + ": " + movimientos);
        panelVictoria.setVisible(true);

        Player player = AuthManager.getCurrentPlayer();
        if (player != null) {
            int puntaje = juego.calcularPuntaje();
            boolean nuevoPuntaje = player.actualizarPuntajeNivel(level, puntaje);

            RankingFileManager.guardarOActualizar(player.getUsername(), puntaje, level);

            String estrellaMsg = nuevoPuntaje ? " ★ " + LanguageManager.get("new_record") : "";
            lblPuntajeVictoria.setText(puntaje + " pts" + estrellaMsg);
            if (nuevoPuntaje) lblPuntajeVictoria.setColor(new Color(0.651f, 0.890f, 0.631f, 1f));

            if (level >= player.getCurrentLevel() && level < 5) {
                player.setCurrentLevel(level + 1);
            }
            player.setTotalTimePlayed(player.getTotalTimePlayed() + (long) tiempoTotal);
            player.setNivelesCompletados(player.getNivelesCompletados() + 1);
            player.setTiempoTotalNiveles(player.getTiempoTotalNiveles() + (long) tiempoTotal);
            player.addGameHistory(LanguageManager.get("level") + " " + level + " - " +
                LanguageManager.get("time") + ": " + seg + "s - " +
                LanguageManager.get("movements") + ": " + movimientos + " - " + puntaje + " pts");
            FileManager.savePlayer(player);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.118f, 0.118f, 0.180f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!juego.isGano() && juego.getMovimientos() > 0) {
            float tiempoTotal = juego.getTiempoTotal();
            int seg = (int) tiempoTotal;
            int centesimas = (int) ((tiempoTotal - seg) * 100);
            lblTiempo.setText(LanguageManager.get("time") + " : " + seg + "." + String.format("%02d", centesimas) + "s");
            lblMovimientos.setText(LanguageManager.get("movements") + ": " + juego.getMovimientos());
        }

        handleInput();
        juego.update(delta);

        batch.begin();
        nivelActual.render(batch, TILE_SIZE, offsetX, offsetY);
        jugador.render(batch, TILE_SIZE, offsetX, offsetY, nivelActual.getFilas());
        batch.end();

        if (!juego.isGano() && juego.verificarVictoria()) {
            procesarVictoria();
        }

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
        juego.detener();
        stage.dispose();
        skin.dispose();
        batch.dispose();
        nivelActual.dispose();
        jugador.dispose();
    }
}