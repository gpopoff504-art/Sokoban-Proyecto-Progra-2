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
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 *
 * @author gpopo
 */
public class LoginScreen implements Screen {

    private final Main game;
    private Stage stage;
    private Skin skin;

    private TextField txtUsername;
    private TextField txtPassword;
    private Label lblMessage;
    private TextButton btnTogglePassword;
    private boolean passwordVisible = false;

    private final AuthManager authManager = new AuthManager();

    public LoginScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(skin.newDrawable("white", new Color(0.118f, 0.118f, 0.180f, 1f)));
        stage.addActor(root);

        Label lblTitle = new Label("SOKOBAN", skin, "default");
        lblTitle.setColor(new Color(0.537f, 0.863f, 0.922f, 1f));
        lblTitle.setFontScale(2.2f);

        Label lblSub = new Label(LanguageManager.get("login"), skin);
        lblSub.setColor(new Color(0.804f, 0.831f, 0.957f, 1f));

        Label lblUser = new Label(LanguageManager.get("username"), skin);
        lblUser.setColor(Color.WHITE);

        txtUsername = new TextField("", skin);
        txtUsername.setMessageText(LanguageManager.get("username"));

        Label lblPass = new Label(LanguageManager.get("password"), skin);
        lblPass.setColor(Color.WHITE);

        txtPassword = new TextField("", skin);
        txtPassword.setMessageText(LanguageManager.get("password"));
        txtPassword.setPasswordMode(true);
        txtPassword.setPasswordCharacter('*');

        btnTogglePassword = new TextButton(LanguageManager.get("show_password"), skin);

        lblMessage = new Label("", skin);
        lblMessage.setColor(new Color(0.953f, 0.545f, 0.659f, 1f));

        TextButton btnLogin    = new TextButton(LanguageManager.get("login"), skin);
        TextButton btnRegister = new TextButton(LanguageManager.get("create_account"), skin);

        root.pad(40);
        root.add(lblTitle).colspan(2).center().padBottom(4).row();
        root.add(lblSub).colspan(2).center().padBottom(30).row();
        root.add(lblUser).colspan(2).left().padBottom(4).row();
        root.add(txtUsername).colspan(2).width(320).padBottom(16).row();
        root.add(lblPass).colspan(2).left().padBottom(4).row();
        root.add(txtPassword).width(220).padBottom(8);
        root.add(btnTogglePassword).width(92).height(36).padLeft(8).padBottom(8).row();
        root.add(lblMessage).colspan(2).center().padBottom(8).row();
        root.add(btnLogin).colspan(2).width(320).height(48).padBottom(10).row();
        root.add(btnRegister).colspan(2).width(320).height(48).row();

        btnTogglePassword.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                passwordVisible = !passwordVisible;
                txtPassword.setPasswordMode(!passwordVisible);
                btnTogglePassword.setText(passwordVisible
                    ? LanguageManager.get("hide_password")
                    : LanguageManager.get("show_password"));
            }
        });

        btnLogin.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handleLogin();
            }
        });

        btnRegister.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Screen oldScreen = game.getScreen();
                game.setScreen(new RegisterScreen(game));
                if (oldScreen != null) oldScreen.dispose();
            }
        });
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Completa todos los campos.");
            return;
        }

        if (authManager.login(username, password)) {
            Player p = AuthManager.getCurrentPlayer();
            if (p != null && !p.isActiva()) {
                mostrarDialogoReactivar(p);
            } else {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        } else {
            lblMessage.setText(LanguageManager.get("err_pass_wrong"));
        }
    }

    private void mostrarDialogoReactivar(Player player) {
        lblMessage.setText(LanguageManager.get("account_deactivated"));
        lblMessage.setColor(new Color(0.976f, 0.886f, 0.686f, 1f));

        TextButton btnSi = new TextButton(LanguageManager.get("reactivate"), skin);
        TextButton btnNo = new TextButton(LanguageManager.get("cancel"), skin);

        Table dialogo = new Table();
        dialogo.setFillParent(true);
        dialogo.setBackground(skin.newDrawable("white", new Color(0f, 0f, 0f, 0.7f)));
        stage.addActor(dialogo);

        dialogo.center();
        dialogo.add(new Label(LanguageManager.get("account_deactivated"), skin)).padBottom(16).row();
        dialogo.add(btnSi).width(200).height(48).padBottom(8).row();
        dialogo.add(btnNo).width(200).height(48).row();

        btnSi.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                player.setActiva(true);
                if (player.getFriends() != null) {
                    for (String amigo : player.getFriends()) {
                        Player pAmigo = FileManager.loadPlayer(amigo);
                        if (pAmigo != null && !pAmigo.getFriends().contains(player.getUsername())) {
                            pAmigo.addFriend(player.getUsername());
                            FileManager.savePlayer(pAmigo);
                        }
                    }
                }
                if (player.getSolicitudesPendientes() != null) {
                    player.getSolicitudesPendientes().clear();
                }
                FileManager.savePlayer(player);
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        btnNo.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AuthManager.setCurrentPlayer(null);
                dialogo.remove();
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.118f, 0.118f, 0.180f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        if (skin != null) skin.dispose();
    }
}
