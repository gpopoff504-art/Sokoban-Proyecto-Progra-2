/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban;

import com.Sokoban.filehandling.FileManager;
import com.Sokoban.model.AuthManager;
import com.Sokoban.model.Language;
import com.Sokoban.model.LanguageManager;
import com.Sokoban.model.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 *
 * @author gpopo
 */
public class SettingsScreen extends BaseScreen {

    private static final Color BG = new Color(0.118f, 0.118f, 0.180f, 1f);
    private static final Color CYAN = new Color(0.537f, 0.863f, 0.922f, 1f);
    private static final Color MUTED = new Color(0.651f, 0.678f, 0.784f, 1f);
    private static final Color GOLD = new Color(0.976f, 0.886f, 0.686f, 1f);
    private static final Color OK_GREEN = new Color(0.400f, 0.850f, 0.500f, 1f);
    private static final Color ERR_RED = new Color(0.950f, 0.380f, 0.380f, 1f);

    private final Main game;
    private Stage stage;
    private Skin skin;

    private Label lblTitle;
    private Label lblUsernameSection;
    private Label lblPasswordSection;
    private Label lblLanguageSection;
    private Label lblVolumeSection;
    private Label lblVolumePct;
    private Label lblFeedback;

    private TextField tfNewUsername;
    private TextField tfOldPassword;
    private TextField tfNewPassword;
    private TextButton btnChangeUsername;
    private TextButton btnChangePassword;
    private TextButton btnSpanish;
    private TextButton btnEnglish;
    private TextButton btnBack;
    private Slider volumeSlider;

    public SettingsScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        iniciarBase();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        buildUI();
    }

    private void buildUI() {
        Player player = AuthManager.getCurrentPlayer();
        if (player == null) {
            Screen oldScreen = game.getScreen();
            game.setScreen(new LoginScreen(game));
            if (oldScreen != null) oldScreen.dispose();
            return;
        }

        LanguageManager.initFromPlayer(player);

        Table root = new Table();
        root.setFillParent(true);
        root.top().pad(30);
        root.setBackground(skin.newDrawable("white", BG));
        stage.addActor(root);

        lblTitle = new Label(LanguageManager.get("settings"), skin);
        lblTitle.setColor(CYAN);
        lblTitle.setFontScale(2.0f);
        root.add(lblTitle).center().padBottom(24).colspan(2).row();

        lblFeedback = new Label("", skin);
        lblFeedback.setColor(OK_GREEN);
        root.add(lblFeedback).center().padBottom(16).colspan(2).row();

        lblUsernameSection = sectionLabel(LanguageManager.get("username"));
        root.add(lblUsernameSection).left().padBottom(4).colspan(2).row();

        tfNewUsername = new TextField("", skin);
        tfNewUsername.setMessageText(LanguageManager.get("new_username"));
        root.add(tfNewUsername).width(280).height(40).padBottom(8);

        btnChangeUsername = new TextButton(LanguageManager.get("change"), skin);
        root.add(btnChangeUsername).width(120).height(40).padLeft(8).padBottom(8).row();

        btnChangeUsername.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handleUsernameChange();
            }
        });

        lblPasswordSection = sectionLabel(LanguageManager.get("password"));
        root.add(lblPasswordSection).left().padTop(16).padBottom(4).colspan(2).row();

        tfOldPassword = new TextField("", skin);
        tfOldPassword.setPasswordMode(true);
        tfOldPassword.setPasswordCharacter('*');
        tfOldPassword.setMessageText(LanguageManager.get("old_password"));
        root.add(tfOldPassword).width(280).height(40).padBottom(6).colspan(2).row();

        tfNewPassword = new TextField("", skin);
        tfNewPassword.setPasswordMode(true);
        tfNewPassword.setPasswordCharacter('*');
        tfNewPassword.setMessageText(LanguageManager.get("new_password"));
        root.add(tfNewPassword).width(280).height(40).padBottom(8);

        btnChangePassword = new TextButton(LanguageManager.get("change"), skin);
        root.add(btnChangePassword).width(120).height(40).padLeft(8).padBottom(8).row();

        btnChangePassword.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handlePasswordChange();
            }
        });

        lblLanguageSection = sectionLabel(LanguageManager.get("language"));
        root.add(lblLanguageSection).left().padTop(16).padBottom(8).colspan(2).row();

        Table langRow = new Table();
        btnSpanish = new TextButton("Espanol", skin);
        btnEnglish = new TextButton("English", skin);
        highlightLanguageButton(player.getLanguage());
        langRow.add(btnSpanish).width(134).height(42).padRight(12);
        langRow.add(btnEnglish).width(134).height(42);
        root.add(langRow).colspan(2).padBottom(8).row();

        btnSpanish.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handleLanguageChange(Language.SPANISH);
            }
        });
        btnEnglish.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handleLanguageChange(Language.ENGLISH);
            }
        });

        lblVolumeSection = sectionLabel(LanguageManager.get("volume"));
        root.add(lblVolumeSection).left().padTop(16).padBottom(4).colspan(2).row();

        int savedPct = Math.round(player.getVolume() * 100f);
        lblVolumePct = new Label(LanguageManager.get("volume_label") + " " + savedPct + "%", skin);
        lblVolumePct.setColor(GOLD);
        root.add(lblVolumePct).left().colspan(2).padBottom(4).row();

        volumeSlider = new Slider(0f, 1f, 0.01f, false, skin);
        volumeSlider.setValue(player.getVolume());
        root.add(volumeSlider).width(280).colspan(2).padBottom(16).row();

        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float v = volumeSlider.getValue();
                int pct = Math.round(v * 100f);
                lblVolumePct.setText(
                    LanguageManager.get("volume_label") + " " + pct + "%"
                );
                Player p = AuthManager.getCurrentPlayer();
                if (p != null) {
                    p.setVolume(v);
                    FileManager.savePlayer(p);
                    com.Sokoban.model.MusicManager.getInstance().setVolume(v);
                }
            }
        });

        btnBack = new TextButton(LanguageManager.get("back"), skin);
        root.add(btnBack).width(280).height(48).colspan(2).padTop(8).row();

        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Screen oldScreen = game.getScreen();
                game.setScreen(new MenuScreen(game));
                if (oldScreen != null) oldScreen.dispose();
            }
        });
    }

    private void handleUsernameChange() {
        Player player = AuthManager.getCurrentPlayer();
        if (player == null) return;

        String newUsername = tfNewUsername.getText().trim();

        if (newUsername.isEmpty()) {
            showFeedback(LanguageManager.get("err_empty"), false);
            return;
        }
        if (newUsername.equals(player.getUsername())) {
            showFeedback(LanguageManager.get("username_ok"), true);
            return;
        }
        if (FileManager.playerExists(newUsername)) {
            showFeedback(LanguageManager.get("err_taken"), false);
            return;
        }

        String oldUsername = player.getUsername();

        boolean folderOk = FileManager.renameUserFolder(oldUsername, newUsername);
        if (!folderOk) {
            showFeedback(LanguageManager.get("error"), false);
            return;
        }

        player.setUsername(newUsername);
        FileManager.savePlayer(player);

        tfNewUsername.setText("");
        showFeedback(LanguageManager.get("username_ok"), true);
    }

    private void handlePasswordChange() {
        Player player = AuthManager.getCurrentPlayer();
        if (player == null) return;

        String oldPass = tfOldPassword.getText();
        String newPass = tfNewPassword.getText();

        if (oldPass.isEmpty() || newPass.isEmpty()) {
            showFeedback(LanguageManager.get("err_empty"), false);
            return;
        }
        if (!player.getPassword().equals(oldPass)) {
            showFeedback(LanguageManager.get("err_pass_wrong"), false);
            return;
        }
        if (newPass.length() < 6) {
            showFeedback(LanguageManager.get("err_pass_short"), false);
            return;
        }

        player.setPassword(newPass);
        FileManager.savePlayer(player);

        tfOldPassword.setText("");
        tfNewPassword.setText("");
        showFeedback(LanguageManager.get("password_ok"), true);
    }

    private void handleLanguageChange(Language lang){
        Player player = AuthManager.getCurrentPlayer();
        if(player == null) return;
        player.setLanguage(lang);
        FileManager.savePlayer(player);
        LanguageManager.setLanguage(lang);
        game.setScreen(new SettingsScreen(game));
        dispose();
    }

    private Label sectionLabel(String text) {
        Label lbl = new Label(text, skin);
        lbl.setColor(MUTED);
        lbl.setFontScale(1.1f);
        return lbl;
    }

    private void showFeedback(String message, boolean success) {
        lblFeedback.setText(message);
        lblFeedback.setColor(success ? OK_GREEN : ERR_RED);
    }

    private void highlightLanguageButton(Language lang) {
        if (btnSpanish == null || btnEnglish == null) return;
        if (lang == Language.SPANISH) {
            btnSpanish.setColor(CYAN);
            btnEnglish.setColor(Color.WHITE);
        } else {
            btnEnglish.setColor(CYAN);
            btnSpanish.setColor(Color.WHITE);
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

    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        disposeBase();
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
    }
}