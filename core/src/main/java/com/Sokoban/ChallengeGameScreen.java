/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban;

import com.Sokoban.filehandling.ChallengeFileManager;
import com.Sokoban.filehandling.FileManager;
import com.Sokoban.model.AuthManager;
import com.Sokoban.model.Jugador;
import com.Sokoban.model.LanguageManager;
import com.Sokoban.model.Level;
import com.Sokoban.model.Level1;
import com.Sokoban.model.Level2;
import com.Sokoban.model.Level3;
import com.Sokoban.model.Level4;
import com.Sokoban.model.Level5;
import com.Sokoban.model.Player;
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

/**
 *
 * @author gpopo
 */
public class ChallengeGameScreen implements Screen {

    private static final Color BG   = new Color(0.118f, 0.118f, 0.180f, 1f);
    private static final Color CYAN = new Color(0.537f, 0.863f, 0.922f, 1f);
    private static final Color GOLD = new Color(0.976f, 0.886f, 0.686f, 1f);
    private static final Color GREEN = new Color(0.651f, 0.890f, 0.631f, 1f);
    private static final Color RED   = new Color(0.953f, 0.545f, 0.659f, 1f);

    private static final int TILE_SIZE = 80;

    private final Main game;
    private final int  nivel;
    private final long challengeIdx;
    private final boolean esRetador;

    private Stage    stage;
    private Skin     skin;
    private SpriteBatch batch;
    private Level    nivelActual;
    private Jugador  jugador;
    private int      offsetX, offsetY;

    private int   movimientos = 0;
    private float tiempoTotal = 0;
    private boolean gano = false;

    private Label lblTiempo;
    private Label lblMovimientos;
    private Table panelResultado;
    private Label lblResultadoTitulo;
    private Label lblScorePropio;
    private Label lblScoreRival;

    public ChallengeGameScreen(Main game, int nivel, long challengeIdx, boolean esRetador) {
        this.game         = game;
        this.nivel        = nivel;
        this.challengeIdx = challengeIdx;
        this.esRetador    = esRetador;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin  = new Skin(Gdx.files.internal("uiskin.json"));
        batch = new SpriteBatch();

        switch (nivel) {
            case 1: nivelActual = new Level1(); break;
            case 2: nivelActual = new Level2(); break;
            case 3: nivelActual = new Level3(); break;
            case 4: nivelActual = new Level4(); break;
            case 5: nivelActual = new Level5(); break;
            default: nivelActual = new Level1();
        }

        offsetX = (Gdx.graphics.getWidth()  - nivelActual.getColumnas() * TILE_SIZE) / 2;
        offsetY = (Gdx.graphics.getHeight() - nivelActual.getFilas()    * TILE_SIZE) / 2;
        jugador = new Jugador(nivelActual.getJugadorX(), nivelActual.getJugadorY());

        Table hud = new Table();
        hud.setFillParent(true);
        hud.top().pad(10);
        stage.addActor(hud);

        Label lblReto = new Label("⚔ " + LanguageManager.get("challenge_mode"), skin);
        lblReto.setColor(GOLD);
        hud.add(lblReto).expandX().left().padLeft(20);

        lblTiempo = new Label(LanguageManager.get("time") + ": 0s", skin);
        lblTiempo.setColor(CYAN);
        hud.add(lblTiempo).expandX().center();

        lblMovimientos = new Label(LanguageManager.get("movements") + ": 0", skin);
        lblMovimientos.setColor(CYAN);
        hud.add(lblMovimientos).expandX().right().padRight(20);

        Table botones = new Table();
        botones.setFillParent(true);
        botones.bottom().pad(10);
        stage.addActor(botones);

        TextButton btnReset = new TextButton(LanguageManager.get("reset"), skin);
        botones.add(btnReset).width(140).height(48);
        btnReset.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new ChallengeGameScreen(game, nivel, challengeIdx, esRetador));
                dispose();
            }
        });

        // Panel resultado (oculto inicialmente)
        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setVisible(false);
        overlay.setBackground(skin.newDrawable("white", new Color(0f, 0f, 0f, 0.75f)));
        stage.addActor(overlay);
        panelResultado = overlay;

        lblResultadoTitulo = new Label("", skin);
        lblResultadoTitulo.setFontScale(2f);
        lblScorePropio = new Label("", skin);
        lblScorePropio.setColor(Color.WHITE);
        lblScoreRival  = new Label("", skin);
        lblScoreRival.setColor(GOLD);

        TextButton btnVolver = new TextButton(LanguageManager.get("back"), skin);
        overlay.center();
        overlay.add(lblResultadoTitulo).padBottom(16).row();
        overlay.add(lblScorePropio).padBottom(8).row();
        overlay.add(lblScoreRival).padBottom(24).row();
        overlay.add(btnVolver).width(240).height(48).row();

        btnVolver.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new ChallengeScreen(game));
                dispose();
            }
        });
    }

    private void handleInput() {
        if (gano) return;
        int dx = 0, dy = 0;
        String dir = null;

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.LEFT)  || Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.A)) { dx = -1; dir = "left";  }
        else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.D)) { dx =  1; dir = "right"; }
        else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.UP)    || Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.W)) { dy = -1; dir = "up";    }
        else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.DOWN)  || Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.S)) { dy =  1; dir = "down";  }

        if (dir != null) moverJugador(dx, dy, dir);
    }

    private void moverJugador(int dx, int dy, String dir) {
        int[][] mapa = nivelActual.getMapa();
        int px = jugador.getGridX(), py = jugador.getGridY();
        int nx = px + dx,           ny = py + dy;

        if (nx < 0 || ny < 0 || ny >= nivelActual.getFilas() || nx >= nivelActual.getColumnas()) return;
        int celda = mapa[ny][nx];
        if (celda == Level.MURO || celda == Level.VACIO) { jugador.setIdle(); return; }

        if (celda == Level.CAJA) {
            int cx = nx + dx, cy = ny + dy;
            if (cx < 0 || cy < 0 || cy >= nivelActual.getFilas() || cx >= nivelActual.getColumnas()) return;
            int detras = mapa[cy][cx];
            if (detras == Level.MURO || detras == Level.CAJA || detras == Level.VACIO) { jugador.setIdle(); return; }
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
        int[][] obj  = nivelActual.getMapaObjetivos();
        for (int f = 0; f < nivelActual.getFilas(); f++)
            for (int c = 0; c < nivelActual.getColumnas(); c++)
                if (obj[f][c] == Level.OBJETIVO && mapa[f][c] != Level.CAJA) return false;
        return true;
    }

    private void procesarVictoria() {
        gano = true;
        int puntaje = Math.max(100, 1000 - (movimientos * 10) - ((int) tiempoTotal * 5));
        Player me = AuthManager.getCurrentPlayer();
        if (me == null) return;

        int scoreRival = 0;
        if (esRetador) {
            ChallengeFileManager.guardarScoreRetador(challengeIdx, puntaje);
            // El retado aun no ha jugado
            lblScoreRival.setText(LanguageManager.get("waiting_rival"));
            ChallengeFileManager.actualizarEstado(challengeIdx, ChallengeFileManager.ACEPTADO);
        } else {
            // El retado termina: comparar con score del retador
            long[] data = obtenerDataReto(challengeIdx);
            scoreRival  = (int) data[2];
            ChallengeFileManager.guardarScoreRetado(challengeIdx, puntaje);
            ChallengeFileManager.actualizarEstado(challengeIdx, ChallengeFileManager.COMPLETADO);
            String rival = ChallengeFileManager.getRetador(challengeIdx);
            if (puntaje > scoreRival) {
                lblResultadoTitulo.setText(LanguageManager.get("challenge_won"));
                lblResultadoTitulo.setColor(GREEN);
            } else if (puntaje < scoreRival) {
                lblResultadoTitulo.setText(LanguageManager.get("challenge_lost"));
                lblResultadoTitulo.setColor(RED);
            } else {
                lblResultadoTitulo.setText(LanguageManager.get("challenge_draw"));
                lblResultadoTitulo.setColor(GOLD);
            }
            lblScoreRival.setText(rival + ": " + scoreRival + " pts");
        }

        if (esRetador) {
            lblResultadoTitulo.setText(LanguageManager.get("challenge_score_saved"));
            lblResultadoTitulo.setColor(CYAN);
        }
        int seg = (int) tiempoTotal;
        lblScorePropio.setText(me.getUsername() + ": " + puntaje + " pts  ·  "
            + seg + "s  ·  " + movimientos + " " + LanguageManager.get("movements"));

        me.addGameHistory("[Reto] " + LanguageManager.get("level") + " " + nivel
            + " - " + puntaje + " pts");
        FileManager.savePlayer(me);

        panelResultado.setVisible(true);
    }

    private long[] obtenerDataReto(long idx) {
        // Reutilizamos getRetosEnviados buscando por idx — solución simple sin romper encapsulamiento
        java.util.List<long[]> lista = ChallengeFileManager.getRetosEnviados(
            ChallengeFileManager.getRetador(idx));
        for (long[] d : lista) {
            if (d[0] == idx) return d;
        }
        return new long[]{idx, nivel, 0, 0, ChallengeFileManager.ACEPTADO};
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(BG.r, BG.g, BG.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!gano && movimientos > 0) {
            tiempoTotal += delta;
            int seg = (int) tiempoTotal;
            int cs  = (int) ((tiempoTotal - seg) * 100);
            lblTiempo.setText(LanguageManager.get("time") + ": " + seg + "." + String.format("%02d", cs) + "s");
            lblMovimientos.setText(LanguageManager.get("movements") + ": " + movimientos);
        }

        handleInput();
        jugador.update(delta);

        batch.begin();
        nivelActual.render(batch, TILE_SIZE, offsetX, offsetY);
        jugador.render(batch, TILE_SIZE, offsetX, offsetY, nivelActual.getFilas());
        batch.end();

        if (!gano && verificarVictoria()) procesarVictoria();

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
        if (stage != null) stage.dispose();
        if (skin  != null) skin.dispose();
        if (batch != null) batch.dispose();
        if (nivelActual != null) nivelActual.dispose();
        if (jugador     != null) jugador.dispose();
    }
}
