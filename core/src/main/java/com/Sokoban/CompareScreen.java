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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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

    private static final Color BG    = new Color(0.118f, 0.118f, 0.180f, 1f);
    private static final Color CYAN  = new Color(0.537f, 0.863f, 0.922f, 1f);
    private static final Color MUTED = new Color(0.651f, 0.678f, 0.784f, 1f);
    private static final Color GREEN = new Color(0.651f, 0.890f, 0.631f, 1f);
    private static final Color RED   = new Color(0.953f, 0.545f, 0.659f, 1f);
    private static final Color GOLD  = new Color(0.976f, 0.886f, 0.686f, 1f);

    private final Main   game;
    private final String friendUsername;
    private Stage   stage;
    private Skin    skin;
    private Texture texMe;
    private Texture texFriend;

    public CompareScreen(Main game, String friendUsername) {
        this.game           = game;
        this.friendUsername = friendUsername;
    }

    @Override
    public void show() {
        iniciarBase();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Player me     = AuthManager.getCurrentPlayer();
        Player friend = FileManager.loadPlayer(friendUsername);

        texMe     = loadAvatar(me);
        texFriend = loadAvatar(friend);

        Table root = new Table();
        root.setFillParent(true);
        root.center().top().pad(24);
        root.setBackground(skin.newDrawable("white", BG));
        stage.addActor(root);

        Table content = new Table();
        root.add(content).center();

        Label lblTitle = new Label(LanguageManager.get("compare_stats"), skin);
        lblTitle.setColor(CYAN);
        lblTitle.setFontScale(1.8f);
        content.add(lblTitle).colspan(3).center().padBottom(20).row();

        if (me == null || friend == null) {
            Label err = new Label(LanguageManager.get("user_not_found"), skin);
            err.setColor(RED);
            content.add(err).colspan(3).center().row();
        } else {
            // Cabecera con nombres y avatares
            Label lblMe     = new Label(me.getUsername(), skin);
            Label lblVs     = new Label("VS", skin);
            Label lblFriend = new Label(friend.getUsername(), skin);
            lblMe.setColor(CYAN);
            lblMe.setFontScale(1.2f);
            lblVs.setColor(MUTED);
            lblFriend.setColor(MUTED);
            lblFriend.setFontScale(1.2f);

            content.add(lblMe).center().width(140);
            content.add(lblVs).center().width(180);
            content.add(lblFriend).center().width(140).row();

            Image imgMe     = new Image(texMe);
            Image imgFriend = new Image(texFriend);
            imgMe.setScaling(com.badlogic.gdx.utils.Scaling.fit);
            imgFriend.setScaling(com.badlogic.gdx.utils.Scaling.fit);

            Table cellMe     = new Table(); cellMe.add(imgMe).size(72, 72);
            Table cellFriend = new Table(); cellFriend.add(imgFriend).size(72, 72);

            content.add(cellMe).width(140).center().padBottom(8);
            content.add().width(180);
            content.add(cellFriend).width(140).center().padBottom(8).row();

            addSep(content);

            // Stats generales
            addStat(content, LanguageManager.get("level"),
                me.getCurrentLevel(), friend.getCurrentLevel(), false, false);
            addStat(content, LanguageManager.get("score"),
                me.getTotalScore(), friend.getTotalScore(), false, true);
            addStat(content, LanguageManager.get("completed_levels"),
                me.getNivelesCompletados(), friend.getNivelesCompletados(), false, false);
            addStat(content, LanguageManager.get("average_time_per_level"),
                (int) me.getTiempoPromedioPorNivel(), (int) friend.getTiempoPromedioPorNivel(), true, false);

            addSep(content);

            // Mejor puntaje por nivel
            Label lblBest = new Label(LanguageManager.get("best_per_level"), skin);
            lblBest.setColor(GOLD);
            content.add(lblBest).colspan(3).center().padTop(6).padBottom(6).row();

            for (int i = 1; i <= 5; i++) {
                addStat(content, LanguageManager.get("level") + " " + i,
                    me.getMejorPuntajeNivel(i), friend.getMejorPuntajeNivel(i), false, true);
            }

            addSep(content);

            // H2H de retos
            int[] h2h = ChallengeFileManager.getH2H(me.getUsername(), friend.getUsername());
            int ganaMe = h2h[0], ganaFriend = h2h[1], empates = h2h[2];
            int totalRetos = ganaMe + ganaFriend + empates;

            Label lblH2H = new Label("H2H " + LanguageManager.get("challenges"), skin);
            lblH2H.setColor(GOLD);
            content.add(lblH2H).colspan(3).center().padTop(6).padBottom(6).row();

            if (totalRetos == 0) {
                Label lblNoRetos = new Label("Sin retos entre ustedes aun", skin);
                lblNoRetos.setColor(MUTED);
                content.add(lblNoRetos).colspan(3).center().padBottom(8).row();
            } else {
                // Marcador H2H tipo "3 - 1 - 2"
                Label lblMeScore  = new Label(String.valueOf(ganaMe), skin);
                Label lblH2HMid   = new Label(empates + " empates", skin);
                Label lblFrScore  = new Label(String.valueOf(ganaFriend), skin);

                lblMeScore.setColor(ganaMe > ganaFriend ? GREEN : (ganaMe < ganaFriend ? RED : Color.WHITE));
                lblMeScore.setFontScale(1.6f);
                lblH2HMid.setColor(MUTED);
                lblFrScore.setColor(ganaFriend > ganaMe ? GREEN : (ganaFriend < ganaMe ? RED : Color.WHITE));
                lblFrScore.setFontScale(1.6f);

                content.add(lblMeScore).center().width(140).padBottom(4);
                content.add(lblH2HMid).center().width(180).padBottom(4);
                content.add(lblFrScore).center().width(140).padBottom(4).row();


            }
        }

        addSep(content);

        TextButton btnBack = new TextButton(LanguageManager.get("back"), skin);
            content.add(btnBack).colspan(3).width(280).height(48).padTop(-27).row();
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

        lName.setWrap(true);
        lName.setAlignment(com.badlogic.gdx.utils.Align.center);

        lMe.setColor(meWins ? GREEN : (friendWins ? RED : Color.WHITE));
        lName.setColor(MUTED);
        lFr.setColor(friendWins ? GREEN : (meWins ? RED : Color.WHITE));

        root.add(lMe).center().width(140).padBottom(8);
        root.add(lName).center().width(180).maxWidth(180).padBottom(8);
        root.add(lFr).center().width(140).padBottom(8).row();
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
    }

    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        disposeBase();
        if (stage     != null) stage.dispose();
        if (skin      != null) skin.dispose();
        if (texMe     != null) texMe.dispose();
        if (texFriend != null) texFriend.dispose();
    }
}