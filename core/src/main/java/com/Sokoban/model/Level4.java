/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban.model;

/**
 *
 * @author Rogelio
 */
public class Level4 extends Level{

    public Level4(){
        cargarSprites();
        cargarMapa();
    }

    @Override
    protected String getRutaMapa(){ return "levels/level4.txt"; }

    @Override
    protected String getRutaMuro(){ return "sprites/Mapa/m4.png"; }

    @Override
    protected String getRutaPiso(){ return "sprites/Mapa/p4.png"; }

    @Override
    protected String getRutaObjetivo(){ return "sprites/Mapa/pc4.png"; }

    @Override
    public String getNombre(){ return "Nivel 4"; }

    @Override
    public int getNivel(){ return 4; }
}