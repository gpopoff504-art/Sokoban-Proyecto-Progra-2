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
    private static final int TILE_SIZE = 80;
    private int offsetX;
    private int offsetY;

    private int movimientos = 0;
    private float tiempoTotal = 0;
    private boolean gano = false;

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

        // Botones abajo
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
                    player.setTotalTimePlayed(player.getTotalTimePlayed() + (long) tiempoTotal);
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
                    player.setTotalTimePlayed(player.getTotalTimePlayed() + (long) tiempoTotal);
                    FileManager.savePlayer(player);
                }
                game.setScreen(new GameScreen(game, level));
                dispose();
            }
        });

        // Panel victoria
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
        if (gano) return;
        int dx = 0, dy = 0;
        String dir = null;

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.LEFT) ||
                Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.A)) {
            dx = -1; dir = "left";
        } else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.RIGHT) ||
                Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.D)) {
            dx = 1; dir = "right";
        } else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.UP) ||
                Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.W)) {
            dy = -1; dir = "up";
        } else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.DOWN) ||
                Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.S)) {
            dy = 1; dir = "down";
        }

        if (dir != null) {
            moverJugador(dx, dy, dir);
        }
    }

    private void moverJugador(int dx, int dy, String dir) {
        int[][] mapa = nivelActual.getMapa();
        int px = jugador.getGridX();
        int py = jugador.getGridY();
        int nx = px + dx;
        int ny = py + dy;

        if (nx < 0 || ny < 0 || ny >= nivelActual.getFilas() || nx >= nivelActual.getColumnas()) return;

        int celda = mapa[ny][nx];

        if (celda == Level.MURO || celda == Level.VACIO) {
            jugador.setIdle();
            return;
        }

        if (celda == Level.CAJA) {
            int cx = nx + dx;
            int cy = ny + dy;
            if (cx < 0 || cy < 0 || cy >= nivelActual.getFilas() || cx >= nivelActual.getColumnas()) return;
            int detras = mapa[cy][cx];
            if (detras == Level.MURO || detras == Level.CAJA || detras == Level.VACIO) {
                jugador.setIdle();
                return;
            }
            mapa[cy][cx] = Level.CAJA;
            mapa[ny][nx] = (nivelActual.getMapaObjetivos()[ny][nx] == Level.OBJETIVO) ? Level.OBJETIVO : Level.PISO;
            jugador.setMoviendo(dir, true);
        } else {
            jugador.setMoviendo(dir, false);
        }

        jugador.setGridX(nx);
        jugador.setGridY(ny);
        movimientos++;
    }

    private boolean verificarVictoria() {
        int[][] mapa = nivelActual.getMapa();
        int[][] mapaOriginal = nivelActual.getMapaObjetivos();
        for (int f = 0; f < nivelActual.getFilas(); f++) {
            for (int c = 0; c < nivelActual.getColumnas(); c++) {
                if (mapaOriginal[f][c] == Level.OBJETIVO && mapa[f][c] != Level.CAJA) {
                    return false;
                }
            }
        }
        return true;
    }

    private void procesarVictoria() {
        gano = true;

        int seg = (int) tiempoTotal;
        int centesimas = (int) ((tiempoTotal - seg) * 100);
        lblTiempoVictoria.setText(LanguageManager.get("time") + ": " + seg + "." + String.format("%02d", centesimas) + "s");
        lblMovsVictoria.setText(LanguageManager.get("movements") + ": " + movimientos);
        panelVictoria.setVisible(true);

        Player player = AuthManager.getCurrentPlayer();
        if (player != null) {
            // Calcular puntaje: base 1000, -10 por movimiento, -5 por segundo, minimo 100
            int puntaje = Math.max(100, 1000 - (movimientos * 10) - ((int) tiempoTotal * 5));
            boolean nuevoPuntaje = player.actualizarPuntajeNivel(level, puntaje);

            // Guardar en ranking global
            RankingFileManager.guardarOActualizar(player.getUsername(), puntaje, level);

            // Mostrar puntaje en panel
            String estrellaMsg = nuevoPuntaje ? " ★ " + LanguageManager.get("new_record") : "";
            lblPuntajeVictoria.setText(puntaje + " pts" + estrellaMsg);
            if (nuevoPuntaje) lblPuntajeVictoria.setColor(new Color(0.651f, 0.890f, 0.631f, 1f));

            // Actualizar progreso
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

        if (!gano && movimientos > 0) {
            tiempoTotal += delta;
            int seg = (int) tiempoTotal;
            int centesimas = (int) ((tiempoTotal - seg) * 100);
            lblTiempo.setText(LanguageManager.get("time") + " : " + seg + "." + String.format("%02d", centesimas) + "s");
            lblMovimientos.setText(LanguageManager.get("movements") + ": " + movimientos);
        }

        handleInput();
        jugador.update(delta);

        batch.begin();
        nivelActual.render(batch, TILE_SIZE, offsetX, offsetY);
        jugador.render(batch, TILE_SIZE, offsetX, offsetY, nivelActual.getFilas());
        batch.end();

        if (!gano && verificarVictoria()) {
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
        stage.dispose();
        skin.dispose();
        batch.dispose();
        nivelActual.dispose();
        jugador.dispose();
    }
}