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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
/**
 *
 * @author Rogelio
 */
public class FriendsScreen extends BaseScreen{

    private final Main game;
    private Stage stage;
    private Skin skin;
    private Label lblMensaje;

    public FriendsScreen(Main game){
        this.game = game;
    }

    @Override
    public void show(){
        iniciarBase();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Player player = FileManager.loadPlayer(AuthManager.getCurrentPlayer().getUsername());
        AuthManager.setCurrentPlayer(player);
        if(player.getSolicitudesPendientes() != null){
            player.getSolicitudesPendientes().removeIf(s -> {
                Player p = FileManager.loadPlayer(s);
                return p == null || !p.isActiva();
            });
            FileManager.savePlayer(player);
        }
        if(player.getFriends() != null){
            player.getFriends().removeIf(f -> {
                Player p = FileManager.loadPlayer(f);
                return p == null || !p.isActiva();
            });
            FileManager.savePlayer(player);
        }
        Table root = new Table();
        root.setFillParent(true);
        root.top().pad(30);
        root.setBackground(skin.newDrawable("white", new Color(0.118f, 0.118f, 0.180f, 1f)));
        stage.addActor(root);

        Label lblTitle = new Label(LanguageManager.get("friends"), skin);
        lblTitle.setColor(new Color(0.537f, 0.863f, 0.922f, 1f));
        lblTitle.setFontScale(1.8f);
        root.add(lblTitle).center().padBottom(20).colspan(2).row();

        Label lblBuscar = new Label(LanguageManager.get("add_friend"), skin);
        lblBuscar.setColor(new Color(0.804f, 0.831f, 0.957f, 1f));
        root.add(lblBuscar).left().padBottom(4).colspan(2).row();

        TextField txtBuscar = new TextField("", skin);
        txtBuscar.setMessageText(LanguageManager.get("search_user"));
        TextButton btnEnviar = new TextButton(LanguageManager.get("send_request"), skin);

        root.add(txtBuscar).width(220).height(40).padBottom(8).padRight(8);
        root.add(btnEnviar).width(140).height(40).padBottom(8).row();

        lblMensaje = new Label("", skin);
        lblMensaje.setColor(new Color(0.651f, 0.890f, 0.631f, 1f));
        root.add(lblMensaje).colspan(2).center().padBottom(16).row();

        btnEnviar.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                String username = txtBuscar.getText().trim();
                enviarSolicitud(player, username);
            }
        });

        Label lblInbox = new Label(LanguageManager.get("inbox"), skin);
        lblInbox.setColor(new Color(0.804f, 0.831f, 0.957f, 1f));
        root.add(lblInbox).left().padBottom(4).colspan(2).row();

        Table tblSolicitudes = new Table();
        if(player.getSolicitudesPendientes() == null || player.getSolicitudesPendientes().isEmpty()){
            Label lbl = new Label(LanguageManager.get("no_requests"), skin);
            lbl.setColor(Color.WHITE);
            tblSolicitudes.add(lbl).row();
        }else{
            for(String solicitante : player.getSolicitudesPendientes()){
                Label lblNombre = new Label(solicitante, skin);
                lblNombre.setColor(Color.WHITE);
                TextButton btnAceptar = new TextButton(LanguageManager.get("accept"), skin);
                TextButton btnRechazar = new TextButton(LanguageManager.get("reject"), skin);

                btnAceptar.addListener(new ChangeListener(){
                    @Override
                    public void changed(ChangeEvent event, Actor actor){
                        aceptarSolicitud(player, solicitante);
                    }
                });

                btnRechazar.addListener(new ChangeListener(){
                    @Override
                    public void changed(ChangeEvent event, Actor actor){
                        rechazarSolicitud(player, solicitante);
                    }
                });

                tblSolicitudes.add(lblNombre).left().width(160).padBottom(8);
                tblSolicitudes.add(btnAceptar).width(90).height(36).padRight(6).padBottom(8);
                tblSolicitudes.add(btnRechazar).width(90).height(36).padBottom(8).row();
            }
        }

        ScrollPane scrollSolicitudes = new ScrollPane(tblSolicitudes, skin);
        scrollSolicitudes.setScrollingDisabled(true, false);
        root.add(scrollSolicitudes).colspan(2).width(460).height(120).padBottom(16).row();

        // Lista de amigos
        Label lblAmigos = new Label(LanguageManager.get("friends"), skin);
        lblAmigos.setColor(new Color(0.804f, 0.831f, 0.957f, 1f));
        root.add(lblAmigos).left().padBottom(4).colspan(2).row();

        Table tblAmigos = new Table();
        if(player.getFriends() == null || player.getFriends().isEmpty()){
            Label lbl = new Label(LanguageManager.get("no_friends"), skin);
            lbl.setColor(Color.WHITE);
            tblAmigos.add(lbl).row();
        }else{
            for(String amigo : player.getFriends()){
                Player pAmigo = FileManager.loadPlayer(amigo);
                if(pAmigo != null && pAmigo.isActiva()){
                    String info = amigo + "  |  " + LanguageManager.get("level") + ": "
                        + pAmigo.getCurrentLevel() + "  |  " + pAmigo.getTotalScore() + " pts";
                    Label lblAmigo = new Label(info, skin);
                    lblAmigo.setColor(Color.WHITE);

                    TextButton btnComparar = new TextButton(LanguageManager.get("compare"), skin);
                    TextButton btnRetar    = new TextButton(LanguageManager.get("challenge"), skin);
                    btnRetar.setColor(new Color(0.537f, 0.863f, 0.922f, 1f));
                    TextButton btnEliminar = new TextButton(LanguageManager.get("remove_friend"), skin);
                    btnEliminar.setColor(new Color(0.950f, 0.380f, 0.380f, 1f));

                    btnComparar.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            game.setScreen(new CompareScreen(game, amigo));
                            dispose();
                        }
                    });

                    btnRetar.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            game.setScreen(new SendChallengeScreen(game, amigo));
                            dispose();
                        }
                    });

                    btnEliminar.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            eliminarAmigo(player, amigo);
                        }
                    });

                    tblAmigos.add(lblAmigo).left().width(200).padBottom(8);
                    tblAmigos.add(btnComparar).width(80).height(36).padRight(4).padBottom(8);
                    tblAmigos.add(btnRetar).width(70).height(36).padRight(4).padBottom(8);
                    tblAmigos.add(btnEliminar).width(80).height(36).padBottom(8).row();
                }
            }
        }

        ScrollPane scrollAmigos = new ScrollPane(tblAmigos, skin);
        scrollAmigos.setScrollingDisabled(true, false);
        root.add(scrollAmigos).colspan(2).width(460).height(160).padBottom(16).row();

        TextButton btnBack = new TextButton(LanguageManager.get("back"), skin);
        root.add(btnBack).colspan(2).width(280).height(48).padTop(8).row();

        btnBack.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });
    }
    private void eliminarAmigo(Player jugador, String amigo){
        jugador.removeFriend(amigo);
        FileManager.savePlayer(jugador);
        Player pAmigo = FileManager.loadPlayer(amigo);
        if(pAmigo != null){
            pAmigo.removeFriend(jugador.getUsername());
            FileManager.savePlayer(pAmigo);
        }
        game.setScreen(new FriendsScreen(game));
        dispose();
    }
    private void enviarSolicitud(Player jugador, String username){
        if(username.isEmpty() || username.equals(jugador.getUsername())){
            mostrarMensaje(LanguageManager.get("user_not_found"), false);
            return;
        }
        if(jugador.getFriends().contains(username)){
            mostrarMensaje(LanguageManager.get("already_friends"), false);
            return;
        }
        if(!FileManager.playerExists(username)){
            mostrarMensaje(LanguageManager.get("user_not_found"), false);
            return;
        }
        Player receptor = FileManager.loadPlayer(username);
        if(!receptor.isActiva()){
            mostrarMensaje(LanguageManager.get("user_not_found"), false);
            return;
        }
        if(receptor.getSolicitudesPendientes() == null){
            receptor.setSolicitudesPendientes(new java.util.ArrayList<>());
        }
        if(receptor.getSolicitudesPendientes().contains(jugador.getUsername())){
            mostrarMensaje(LanguageManager.get("request_exists"), false);
            return;
        }
        if(jugador.getSolicitudesPendientes() != null && jugador.getSolicitudesPendientes().contains(username)){
            jugador.getSolicitudesPendientes().remove(username);
            if(!jugador.getFriends().contains(username)){
                jugador.addFriend(username);
            }
            if(!receptor.getFriends().contains(jugador.getUsername())){
                receptor.addFriend(jugador.getUsername());
            }
            FileManager.savePlayer(jugador);
            FileManager.savePlayer(receptor);
            mostrarMensaje(LanguageManager.get("already_friends"), true);
            return;
        }
        
        receptor.agregarSolicitud(jugador.getUsername());
        FileManager.savePlayer(receptor);
        mostrarMensaje(LanguageManager.get("request_sent"), true);
    }

    private void aceptarSolicitud(Player jugador, String solicitante){
        jugador.eliminarSolicitud(solicitante);
        if(!jugador.getFriends().contains(solicitante)){
            jugador.addFriend(solicitante);
        }
        FileManager.savePlayer(jugador);
        Player amigo = FileManager.loadPlayer(solicitante);
        if(amigo != null){
            amigo.eliminarSolicitud(jugador.getUsername());
            if(!amigo.getFriends().contains(jugador.getUsername())){
                amigo.addFriend(jugador.getUsername());
            }
            FileManager.savePlayer(amigo);
        }
        game.setScreen(new FriendsScreen(game));
        dispose();
    }
    private void rechazarSolicitud(Player jugador, String solicitante){
        jugador.eliminarSolicitud(solicitante);
        FileManager.savePlayer(jugador);
        game.setScreen(new FriendsScreen(game));
        dispose();
    }

    private void mostrarMensaje(String msg, boolean exito){
        lblMensaje.setText(msg);
        lblMensaje.setColor(exito ? new Color(0.651f, 0.890f, 0.631f, 1f) : new Color(0.953f, 0.545f, 0.659f, 1f));
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0.118f, 0.118f, 0.180f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        dibujarHUD();
    }

    @Override
    public void resize(int width, int height){
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause(){}
    @Override public void resume(){}
    @Override public void hide(){}

    @Override
    public void dispose(){
        disposeBase();
        stage.dispose();
        skin.dispose();
    }
}