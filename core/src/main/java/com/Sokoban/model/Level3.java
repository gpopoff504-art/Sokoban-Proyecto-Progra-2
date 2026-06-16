/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban.model;

/**
 *
 * @author Rogelio
 */
public class Level3 extends Level{

    public Level3(){
        cargarSprites();
        cargarMapa();
    }

    @Override
    protected String getRutaMapa(){ return "levels/level3.txt"; }

    @Override
    protected String getRutaMuro(){ return "sprites/Mapa/m3.png"; }

    @Override
    protected String getRutaPiso(){ return "sprites/Mapa/p3.png"; }

    @Override
    protected String getRutaObjetivo(){ return "sprites/Mapa/pc3.png"; }

    @Override
    public String getNombre(){ return "Nivel 3"; }

    @Override
    public int getNivel(){ return 3; }
}