/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban.model;

/**
 *
 * @author Rogelio
 */
public class Level5 extends Level{

    public Level5(){
        cargarSprites();
        cargarMapa();
    }

    @Override
    protected String getRutaMapa(){ return "levels/level5.txt"; }

    @Override
    protected String getRutaMuro(){ return "sprites/Mapa/m5.png"; }

    @Override
    protected String getRutaPiso(){ return "sprites/Mapa/p5.png"; }

    @Override
    protected String getRutaObjetivo(){ return "sprites/Mapa/pc5.png"; }

    @Override
    public String getNombre(){ return "Nivel 5"; }

    @Override
    public int getNivel(){ return 5; }
}