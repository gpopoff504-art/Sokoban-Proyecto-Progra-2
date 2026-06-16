/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Sokoban.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author gpopo
 */
public class LanguageManager {

    private static Language current = Language.SPANISH;

    // Llaves existentes
    public static final String KEY_SETTINGS       = "settings";
    public static final String KEY_USERNAME       = "username";
    public static final String KEY_NEW_USERNAME   = "new_username";
    public static final String KEY_PASSWORD       = "password";
    public static final String KEY_OLD_PASSWORD   = "old_password";
    public static final String KEY_NEW_PASSWORD   = "new_password";
    public static final String KEY_CONFIRM        = "confirm";
    public static final String KEY_CHANGE         = "change";
    public static final String KEY_LANGUAGE       = "language";
    public static final String KEY_VOLUME         = "volume";
    public static final String KEY_BACK           = "back";
    public static final String KEY_SAVE           = "save";
    public static final String KEY_SUCCESS        = "success";
    public static final String KEY_ERROR          = "error";
    public static final String KEY_ERR_EMPTY      = "err_empty";
    public static final String KEY_ERR_TAKEN      = "err_taken";
    public static final String KEY_ERR_PASS_SHORT = "err_pass_short";
    public static final String KEY_ERR_PASS_WRONG = "err_pass_wrong";
    public static final String KEY_USERNAME_OK    = "username_ok";
    public static final String KEY_PASSWORD_OK    = "password_ok";
    public static final String KEY_VOLUME_LABEL   = "volume_label";

    // NUEVAS LLAVES PARA EL MENÚ PRINCIPAL
    public static final String KEY_WELCOME        = "welcome";
    public static final String KEY_LEVEL          = "level";
    public static final String KEY_PLAY           = "play";
    public static final String KEY_PROFILE        = "profile";
    public static final String KEY_LOGOUT         = "logout";

    private static final Map<String, String> ES = new HashMap<>();
    private static final Map<String, String> EN = new HashMap<>();

    static {
        // Se usa \u00f1 para la 'ñ' y escape de caracteres para evitar fallos de fuentes
        ES.put(KEY_SETTINGS,       "Ajustes");
        ES.put(KEY_USERNAME,       "Usuario");
        ES.put(KEY_NEW_USERNAME,   "Nuevo usuario");
        ES.put(KEY_PASSWORD,       "Contrase\u00f1a");
        ES.put(KEY_OLD_PASSWORD,   "Contrase\u00f1a actual");
        ES.put(KEY_NEW_PASSWORD,   "Nueva contrase\u00f1a");
        ES.put(KEY_CONFIRM,        "Confirmar");
        ES.put(KEY_CHANGE,         "Cambiar");
        ES.put(KEY_LANGUAGE,       "Idioma");
        ES.put(KEY_VOLUME,         "Volumen");
        ES.put(KEY_BACK,           "Volver");
        ES.put(KEY_SAVE,           "Guardar");
        ES.put(KEY_SUCCESS,        "¡Listo!");
        ES.put(KEY_ERROR,          "Error");
        ES.put(KEY_ERR_EMPTY,      "El campo no puede estar vac\u00edo.");
        ES.put(KEY_ERR_TAKEN,      "Ese nombre de usuario ya existe.");
        ES.put(KEY_ERR_PASS_SHORT, "La contrase\u00f1a debe tener al menos 6 caracteres.");
        ES.put(KEY_ERR_PASS_WRONG, "La contrase\u00f1a actual es incorrecta.");
        ES.put(KEY_USERNAME_OK,    "Usuario actualizado correctamente.");
        ES.put(KEY_PASSWORD_OK,    "Contrase\u00f1a actualizada correctamente.");
        ES.put(KEY_VOLUME_LABEL,   "Volumen: ");
        
        // Traducciones para el menú en Español
        ES.put(KEY_WELCOME,        "Hola, ");
        ES.put(KEY_LEVEL,          "Nivel ");
        ES.put(KEY_PLAY,           "Jugar");
        ES.put(KEY_PROFILE,        "Mi Perfil");
        ES.put(KEY_LOGOUT,         "Cerrar Sesion");

        EN.put(KEY_SETTINGS,       "Settings");
        EN.put(KEY_USERNAME,       "Username");
        EN.put(KEY_NEW_USERNAME,   "New username");
        EN.put(KEY_PASSWORD,       "Password");
        EN.put(KEY_OLD_PASSWORD,   "Current password");
        EN.put(KEY_NEW_PASSWORD,   "New password");
        EN.put(KEY_CONFIRM,        "Confirm");
        EN.put(KEY_CHANGE,         "Change");
        EN.put(KEY_LANGUAGE,       "Language");
        EN.put(KEY_VOLUME,         "Volume");
        EN.put(KEY_BACK,           "Back");
        EN.put(KEY_SAVE,           "Save");
        EN.put(KEY_SUCCESS,        "Done!");
        EN.put(KEY_ERROR,          "Error");
        EN.put(KEY_ERR_EMPTY,      "Field cannot be empty.");
        EN.put(KEY_ERR_TAKEN,      "That username is already taken.");
        EN.put(KEY_ERR_PASS_SHORT, "Password must be at least 6 characters.");
        EN.put(KEY_ERR_PASS_WRONG, "Current password is incorrect.");
        EN.put(KEY_USERNAME_OK,    "Username updated successfully.");
        EN.put(KEY_PASSWORD_OK,    "Password updated successfully.");
        EN.put(KEY_VOLUME_LABEL,   "Volume: ");
        
        // Traducciones para el menú en Inglés
        EN.put(KEY_WELCOME,        "Hello, ");
        EN.put(KEY_LEVEL,          "Level ");
        EN.put(KEY_PLAY,           "Play");
        EN.put(KEY_PROFILE,        "My Profile");
        EN.put(KEY_LOGOUT,         "Log Out");
    }

    public static void setLanguage(Language lang) {
        current = lang;
    }

    public static Language getLanguage() {
        return current;
    }

    public static String get(String key) {
        Map<String, String> table = (current == Language.ENGLISH) ? EN : ES;
        return table.getOrDefault(key, key);
    }

    public static void initFromPlayer(com.Sokoban.model.Player player) {
        if (player != null && player.getLanguage() != null) {
            current = player.getLanguage();
        } else {
            current = Language.SPANISH;
        }
    }
}
