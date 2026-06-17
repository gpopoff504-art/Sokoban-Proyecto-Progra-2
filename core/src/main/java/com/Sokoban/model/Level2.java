/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban.model;
/**
 *
 * @author Rogelio
 */
public class Level2 extends Level{

    public Level2(){
        cargarSprites();
        cargarMapa();
    }

    @Override
    protected String getRutaMapa(){ 
        return "levels/level2.txt"; 
    }

    @Override
    protected String getRutaMuro(){
        return "sprites/Mapa/m2.png"; 
    }

    @Override
    protected String getRutaPiso(){ 
        return "sprites/Mapa/p2.png"; 
    }

    @Override
    protected String getRutaObjetivo(){ 
        return "sprites/Mapa/pc2.png";
    }

    @Override
    public String getNombre(){ 
        return "Nivel 2"; 
    }

    @Override
    public int getNivel(){ return 2;
    }
}