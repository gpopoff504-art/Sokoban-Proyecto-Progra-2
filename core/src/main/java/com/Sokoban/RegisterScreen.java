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
import com.Sokoban.model.LanguageManager;

/**
 *
 * @author gpopo
 */
public class RegisterScreen implements Screen {

    private final Main game;
    private Stage stage;
    private Skin skin;

    private TextField txtUsername;
    private TextField txtFullName;
    private TextField txtPassword;
    private TextField txtConfirm;
    private Label lblMessage;
    private TextButton btnTogglePassword;
    private boolean passwordVisible = false;
    private TextButton btnToggleConfirmPassword;
    private boolean confirmPasswordVisible = false;

    private final AuthManager authManager = new AuthManager();
    
    public RegisterScreen(Main game) {
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

        Label lblTitle = new Label("CREAR CUENTA", skin);
        lblTitle.setColor(new Color(0.537f, 0.863f, 0.922f, 1f));
        lblTitle.setFontScale(1.8f);

        txtUsername = makeField(LanguageManager.get("new_username"));
        txtFullName = makeField("Nombre completo");
        txtPassword = makeField(LanguageManager.get("new_password"));
        txtPassword.setPasswordMode(true);
        txtPassword.setPasswordCharacter('*');
        
        btnTogglePassword = new TextButton(LanguageManager.get("show_password"), skin);
        
       txtConfirm  = makeField("Confirmar contraseña");
        txtConfirm.setPasswordMode(true);
        txtConfirm.setPasswordCharacter('*');

        btnToggleConfirmPassword = new TextButton(LanguageManager.get("show_password"), skin);

        lblMessage = new Label("", skin);
        lblMessage.setColor(new Color(0.953f, 0.545f, 0.659f, 1f));
        TextButton btnRegister = new TextButton("Registrarse", skin);
        TextButton btnBack = new TextButton(LanguageManager.get("back"), skin);
        root.pad(40);
        root.add(lblTitle).center().padBottom(24).row();
        root.add(makeLabel(LanguageManager.get("username"), skin)).left().padBottom(4).row();
        root.add(txtUsername).width(320).padBottom(12).row();
        root.add(makeLabel("Nombre completo", skin)).left().padBottom(4).row();
        root.add(txtFullName).width(320).padBottom(12).row();
        root.add(makeLabel(LanguageManager.get("password"), skin)).left().padBottom(4).row();
        root.add(txtPassword).width(320).padBottom(8);
        root.add(btnTogglePassword).width(92).height(36).padLeft(8).padBottom(8).row();

        root.add(makeLabel("Confirmar contraseña", skin)).left().padBottom(4).row();
        root.add(txtConfirm).width(320).padBottom(8);
        root.add(btnToggleConfirmPassword).width(92).height(36).padLeft(8).padBottom(8).row();

        root.add(lblMessage).colspan(2).center().padBottom(8).row();
        root.add(btnRegister).colspan(2).width(320).height(48).padBottom(10).row();
        root.add(btnBack).colspan(2).width(320).height(48).row();

        btnRegister.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handleRegister();
            }
        });
        
        btnTogglePassword.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                passwordVisible = !passwordVisible;
                txtPassword.setPasswordMode(!passwordVisible);
                btnTogglePassword.setText(passwordVisible
                        ? LanguageManager.get("hide_password")
                        : LanguageManager.get("show_password"));
            }
        });

        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Screen oldScreen = game.getScreen();
                game.setScreen(new LoginScreen(game));
                if (oldScreen != null) oldScreen.dispose();
            }
        });
        
            btnToggleConfirmPassword.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                confirmPasswordVisible = !confirmPasswordVisible;                
                txtConfirm.setPasswordMode(!confirmPasswordVisible);                
                btnToggleConfirmPassword.setText(confirmPasswordVisible              
                    ? LanguageManager.get("hide_password")
                    : LanguageManager.get("show_password"));
            }
        });
    }

    private void handleRegister() {
        String username = txtUsername.getText().trim();
        String fullName = txtFullName.getText().trim();
        String password = txtPassword.getText();
        String confirm  = txtConfirm.getText();

        if (username.isEmpty() || fullName.isEmpty() || password.isEmpty()){
            showMessage("Completa todos los campos.", false);
            return;
        }
        if (!password.equals(confirm)) {
            showMessage("Las contraseñas no coinciden.", false);
            return;
        }
        if (password.length() < 6) {
            showMessage("La contraseña debe tener al menos 6 caracteres.", false);
            return;
        }
        if(!password.matches(".*[0-9].*")){
            showMessage("Contrasena no contiene numeros.", false);
            return;
        }
        if(!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?\\\\|].*")){
            showMessage("Contrasena no contiene caracteres especiales.", false);
            return;
        }
        if(!password.matches(".*[A-Z].*")){
            showMessage("Contrasena no contiene mayusculas.", false);
            return;
        }
        if (authManager.register(username, password, fullName, "aP")) {
            Screen oldScreen = game.getScreen();
            game.setScreen(new AvatarScreen(game, true));
            if (oldScreen != null) oldScreen.dispose();
        } else {
            showMessage(LanguageManager.get("err_taken"), false);
        }
    }

    private void showMessage(String msg, boolean success) {
        lblMessage.setText(msg);
        lblMessage.setColor(success
            ? new Color(0.651f, 0.890f, 0.631f, 1f)
            : new Color(0.953f, 0.545f, 0.659f, 1f));
    }

    private TextField makeField(String hint) {
        TextField f = new TextField("", skin);
        f.setMessageText(hint);
        return f;
    }

    private Label makeLabel(String text, Skin skin) {
        Label l = new Label(text, skin);
        l.setColor(Color.WHITE);
        return l;
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