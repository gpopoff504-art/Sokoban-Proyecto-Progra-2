/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban;

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
import com.Sokoban.model.AuthManager;

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

        Label lblSub = new Label("Iniciar Sesion", skin);
        lblSub.setColor(new Color(0.804f, 0.831f, 0.957f, 1f));

        Label lblUser = new Label("Usuario", skin);
        lblUser.setColor(Color.WHITE);

        txtUsername = new TextField("", skin);
        txtUsername.setMessageText("Ingresa tu usuario");

        Label lblPass = new Label("Contrasena", skin);
        lblPass.setColor(Color.WHITE);

        txtPassword = new TextField("", skin);
        txtPassword.setMessageText("Ingresa tu contrasena");
        txtPassword.setPasswordMode(true);
        txtPassword.setPasswordCharacter('*');

        lblMessage = new Label("", skin);
        lblMessage.setColor(new Color(0.953f, 0.545f, 0.659f, 1f));

        TextButton btnLogin    = new TextButton("Iniciar Sesion",   skin);
        TextButton btnRegister = new TextButton("Crear cuenta nueva", skin);

        root.pad(40);
        root.add(lblTitle).colspan(2).center().padBottom(4).row();
        root.add(lblSub).colspan(2).center().padBottom(30).row();
        root.add(lblUser).left().padBottom(4).row();
        root.add(txtUsername).width(320).padBottom(16).row();
        root.add(lblPass).left().padBottom(4).row();
        root.add(txtPassword).width(320).padBottom(8).row();
        root.add(lblMessage).colspan(2).center().padBottom(8).row();
        root.add(btnLogin).width(320).height(48).padBottom(10).row();
        root.add(btnRegister).width(320).height(48).row();

        btnLogin.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handleLogin();
            }
        });

        btnRegister.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new RegisterScreen(game));
                dispose();
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
            game.setScreen(new MenuScreen(game));
            dispose();
        } else {
            lblMessage.setText("Usuario o contrasena incorrectos.");
        }
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
        stage.dispose();
        skin.dispose();
    }
}
