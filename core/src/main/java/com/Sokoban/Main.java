/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban;

import com.Sokoban.model.LanguageManager;
import com.badlogic.gdx.Game;

/**
 *
 * @author gpopo
 */
public class Main extends Game {

    @Override
    public void create(){
        LanguageManager.init();
        setScreen(new LoginScreen(this));
    }
}