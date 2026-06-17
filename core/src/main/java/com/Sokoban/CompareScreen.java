/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban;

import com.Sokoban.filehandling.FileManager;
import com.Sokoban.model.AuthManager;
import com.Sokoban.model.LanguageManager;
import com.Sokoban.model.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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
public class CompareScreen extends BaseScreen {

    private static final Color BG   = new Color(0.118f, 0.118f, 0.180f, 1f);
    private static final Color CYAN = new Color(0.537f, 0.863f, 0.922f, 1f);
    private static final Color MUTED = new Color(0.651f, 0.678f, 0.784f, 1f);
    private static final Color GREEN = new Color(0.651f, 0.890f, 0.631f, 1f);
    private static final Color RED   = new Color(0.953f, 0.545f, 0.659f, 1f);
    private static final Color GOLD  = new Color(0.976f, 0.886f, 0.686f, 1f);

    private final Main game;
    private final String friendUsername;
    private Stage stage;
    private Skin skin;
    private SpriteBatch batchAvatars;
    private Texture texMe;
    private Texture texFriend;

    public CompareScreen(Main game, String friendUsername) {
        this.game = game;
        this.friendUsername = friendUsername;
    }

    @Override
    public void show() {
        iniciarBase();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        batchAvatars = new SpriteBatch();

        Player me     = AuthManager.getCurrentPlayer();
        Player friend = FileManager.loadPlayer(friendUsername);

        texMe     = loadAvatar(me);
        texFriend = loadAvatar(friend);

        Table root = new Table();
        root.setFillParent(true);
        root.top().pad(24);
        root.setBackground(skin.newDrawable("white", BG));
        stage.addActor(root);

        Label lblTitle = new Label(LanguageManager.get("compare_stats"), skin);
        lblTitle.setColor(CYAN);
        lblTitle.setFontScale(1.8f);
        root.add(lblTitle).colspan(3).center().padBottom(20).row();

        if (me == null || friend == null) {
            Label err = new Label(LanguageManager.get("user_not_found"), skin);
            err.setColor(RED);
            root.add(err).colspan(3).center().row();
        } else {
            Label lblMe     = new Label(me.getUsername(), skin);
            Label lblVs     = new Label("VS", skin);
            Label lblFriend = new Label(friend.getUsername(), skin);
            lblMe.setColor(CYAN);
            lblMe.setFontScale(1.2f);
            lblVs.setColor(MUTED);
            lblFriend.setColor(MUTED);
            lblFriend.setFontScale(1.2f);

            root.add(lblMe).center().width(200);
            root.add(lblVs).center().width(60);
            root.add(lblFriend).center().width(200).row();
            root.add().height(84).width(200);
            root.add().width(60);
            root.add().height(84).width(200).row();

            addSep(root);

            addStat(root, LanguageManager.get("level"),
                me.getCurrentLevel(), friend.getCurrentLevel(), false, false);
            addStat(root, LanguageManager.get("score"),
                me.getTotalScore(), friend.getTotalScore(), false, true);
            addStat(root, LanguageManager.get("completed_levels"),
                me.getNivelesCompletados(), friend.getNivelesCompletados(), false, false);
            addStat(root, LanguageManager.get("average_time_per_level"),
                (int) me.getTiempoPromedioPorNivel(), (int) friend.getTiempoPromedioPorNivel(), true, false);

            addSep(root);

            Label lblBest = new Label(LanguageManager.get("best_per_level"), skin);
            lblBest.setColor(GOLD);
            root.add(lblBest).colspan(3).center().padTop(6).padBottom(6).row();

            for (int i = 1; i <= 5; i++) {
                addStat(root, LanguageManager.get("level") + " " + i,
                    me.getMejorPuntajeNivel(i), friend.getMejorPuntajeNivel(i), false, true);
            }
        }

        addSep(root);

        TextButton btnBack = new TextButton(LanguageManager.get("back"), skin);
        root.add(btnBack).colspan(3).width(280).height(48).padTop(12).row();
        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new FriendsScreen(game));
                dispose();
            }
        });
    }

    private void addSep(Table root) {
        Label sep = new Label("──────────────────────────────────", skin);
        sep.setColor(new Color(0.3f, 0.3f, 0.4f, 1f));
        root.add(sep).colspan(3).center().padTop(2).padBottom(2).row();
    }

    private void addStat(Table root, String nombre, int valMe, int valFriend,
                         boolean lowerBetter, boolean showPts) {
        boolean meWins     = lowerBetter ? valMe < valFriend : valMe > valFriend;
        boolean friendWins = lowerBetter ? valFriend < valMe : valFriend > valMe;
        String sfx = showPts ? " pts" : (lowerBetter ? "s" : "");

        Label lMe   = new Label(valMe + sfx, skin);
        Label lName = new Label(nombre, skin);
        Label lFr   = new Label(valFriend + sfx, skin);

        lMe.setColor(meWins ? GREEN : (friendWins ? RED : Color.WHITE));
        lName.setColor(MUTED);
        lFr.setColor(friendWins ? GREEN : (meWins ? RED : Color.WHITE));

        root.add(lMe).center().width(200).padBottom(8);
        root.add(lName).center().width(60).padBottom(8);
        root.add(lFr).center().width(200).padBottom(8).row();
    }

    private Texture loadAvatar(Player p) {
        if (p == null || p.getAvatarPath() == null
                || p.getAvatarPath().equals("default")
                || p.getAvatarPath().equals("aP")) {
            return new Texture(Gdx.files.internal("avatars/aP.png"));
        }
        return new Texture(Gdx.files.internal("avatars/" + p.getAvatarPath() + ".png"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(BG.r, BG.g, BG.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        batchAvatars.begin();
        batchAvatars.draw(texMe,     w / 2 - 220, h - 222, 72, 72);
        batchAvatars.draw(texFriend, w / 2 + 148, h - 222, 72, 72);
        batchAvatars.end();

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
        if (batchAvatars != null) batchAvatars.dispose();
        if (texMe != null) texMe.dispose();
        if (texFriend != null) texFriend.dispose();
    }
}
