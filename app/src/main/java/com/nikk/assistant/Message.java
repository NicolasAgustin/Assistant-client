package com.nikk.assistant;

import android.util.Log;

import java.util.ArrayList;

public class Message {

    private String titulo;
    private String contenido;

    public Message(String t, String c) {
        this.titulo = t;
        this.contenido = c;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public static Message createMessage(String msg) {
        String description = msg.replaceAll("^\\Q[\\E.*?\\Q]\\E", "");
        Log.i("CREATE_MESSAGE: Desc", description);
        String title = msg.replace(description, "")
                .replace("[","")
                .replace("]","");
        Log.i("CREATE_MESSAGE: tit", title);

        return new Message(title, description);
    }

}
