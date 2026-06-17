/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.Sokoban.model.AuthManager;
import com.Sokoban.model.Player;

/**
 *
 * @author Rogelio
 */
public abstract class BaseScreen implements Screen{

    protected SpriteBatch batchBase;
    private Texture texAvatar;

    protected void iniciarBase(){
        batchBase = new SpriteBatch();
        cargarAvatar();
    }
    
    protected void resetearNivel(){
    }
    
    private void cargarAvatar(){
        Player player = AuthManager.getCurrentPlayer();
        String ruta;
        if(player == null || player.getAvatarPath().equals("default")){
            ruta = "avatars/aP.png";
        }else{
            ruta = "avatars/" + player.getAvatarPath() + ".png";
        }
        texAvatar = new Texture(Gdx.files.internal(ruta));
    }

    protected void dibujarHUD(){
        Player player = AuthManager.getCurrentPlayer();
        if(player == null) return;
        batchBase.begin();
        batchBase.draw(texAvatar, 10, Gdx.graphics.getHeight() - 74, 64, 64);
        batchBase.end();
    }

    protected void recargarAvatar(){
        if(texAvatar != null) texAvatar.dispose();
        cargarAvatar();
    }

    protected void disposeBase(){
        if(batchBase != null) batchBase.dispose();
        if(texAvatar != null) texAvatar.dispose();
    }

    @Override public void show(){}
    @Override public void render(float delta){}
    @Override public void resize(int width, int height){}
    @Override public void pause(){}
    @Override public void resume(){}
    @Override public void hide(){}
    @Override public void dispose(){}
}