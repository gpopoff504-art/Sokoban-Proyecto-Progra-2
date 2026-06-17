/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 *
 * @author gpopo
 */
public class MusicManager {

    private static MusicManager instance;
    private Music current;
    private float volume = 1.0f;
    private boolean muted = false;

    private MusicManager() {}

    public static MusicManager getInstance() {
        if (instance == null) instance = new MusicManager();
        return instance;
    }

    public void play(String rutaArchivo) {
        if (current != null) {
            current.stop();
            current.dispose();
        }
        current = Gdx.audio.newMusic(Gdx.files.internal(rutaArchivo));
        current.setLooping(true);
        current.setVolume(muted ? 0f : volume);
        current.play();
    }

    public void setVolume(float v) {
        volume = Math.max(0f, Math.min(1f, v));
        if (current != null) current.setVolume(muted ? 0f : volume);
    }

    public float getVolume() { return volume; }

    public void setMuted(boolean m) {
        muted = m;
        if (current != null) current.setVolume(muted ? 0f : volume);
    }

    public boolean isMuted() { return muted; }

    public void stop() {
        if (current != null) { current.stop(); current.dispose(); current = null; }
    }

    public void pause() {
        if (current != null && current.isPlaying()) current.pause();
    }

    public void resume() {
        if (current != null && !current.isPlaying()) current.play();
    }

    public boolean isPlaying() {
        return current != null && current.isPlaying();
    }

    public void dispose() {
        stop();
        instance = null;
    }
}
