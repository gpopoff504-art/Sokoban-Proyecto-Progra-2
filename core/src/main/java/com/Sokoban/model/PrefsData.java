/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban.model;

import java.io.Serializable;

/**
 *
 * @author gpopo
 */
public class PrefsData implements Serializable {

    private static final long serialVersionUID = 1L;

    private float volume;
    private Language language;

    public PrefsData() {
        this.volume = 1.0f;
        this.language = Language.SPANISH;
    }

    public float getVolume() { return volume; }
    public void setVolume(float volume) { this.volume = volume; }
    public Language getLanguage() { return language; }
    public void setLanguage(Language language) { this.language = language; }
}
