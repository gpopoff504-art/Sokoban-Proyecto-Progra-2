/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban;

import com.badlogic.gdx.Game;

/**
 *
 * @author gpopo
 */
public class Main extends Game {

    @Override
    public void create() {
        setScreen(new LoginScreen(this));
    }
}