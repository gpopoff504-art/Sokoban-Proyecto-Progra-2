/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban;

import com.Sokoban.model.LanguageManager;
import com.Sokoban.model.MusicManager;
import com.badlogic.gdx.Game;


/**
 *
 * @author gpopo
 */
public class Main extends Game {
    @Override
    public void create(){
        LanguageManager.init();
        MusicManager.getInstance().play("music/background.mp3");
        setScreen(new LoginScreen(this));
    }

    @Override
    public void pause() {
        MusicManager.getInstance().pause();
    }

    @Override
    public void resume() {
        MusicManager.getInstance().resume();
    }

    @Override
    public void dispose() {
        MusicManager.getInstance().dispose();
        super.dispose();
    }
}