/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;
import java.util.Locale;

/**
 *
 * @author gpopo
 */

public class LanguageManager{

    private static I18NBundle bundle;
    private static Language current = Language.SPANISH;

    public static void init(){
        cargarBundle();
    }

    private static void cargarBundle(){
        try{
            Locale locale = (current == Language.ENGLISH) ? new Locale("en") : new Locale("es");          
            bundle = I18NBundle.createBundle(Gdx.files.internal("strings"), locale);
        }catch(Exception e){
            System.err.println("Error cargando bundle: " + e.getMessage());
            bundle = null;
        }
    }

    public static void setLanguage(Language lang){
        current = lang;
        cargarBundle();
    }

    public static Language getLanguage(){
        return current;
    }

    public static String get(String key){
        if(bundle == null) return key;
        try{
            return bundle.get(key);
        }catch(Exception e){
            return key;
        }
    }

    public static void initFromPlayer(Player player){
        if(player != null && player.getLanguage() != null){
            current = player.getLanguage();
        }else{
            current = Language.SPANISH;
        }
        cargarBundle();
    }
}