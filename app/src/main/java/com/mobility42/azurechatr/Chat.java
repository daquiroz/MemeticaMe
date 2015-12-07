package com.mobility42.azurechatr;

import java.util.*;

public class Chat {

    @com.google.gson.annotations.SerializedName("namechat")
    private String namechat;

    @com.google.gson.annotations.SerializedName("idcreador")
    private String idcreador;

    @com.google.gson.annotations.SerializedName("idcontact")
    private String idcontact;

    @com.google.gson.annotations.SerializedName("idchat")
    private String idchat;

    @com.google.gson.annotations.SerializedName("fecha")
    private String fecha;

    @com.google.gson.annotations.SerializedName("id")
    private String id;

    public String getNamechat() {
        return namechat;
    }

    public void setNamechat(String namechat) {
        this.namechat = namechat;
    }

    public String getIdcreador() {
        return idcreador;
    }

    public void setIdcreador(String idcreador) {
        this.idcreador = idcreador;
    }

    public String getIdchat() {
        return idchat;
    }

    public void setIdchat(String idchat) {
        this.idchat = idchat;
    }

    public String getIdcontact() {
        return idcontact;
    }

    public void setIdcontact(String idcontact) {
        this.idcontact = idcontact;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Chat() {

    }


    public Chat(String namechat, String idcreador, String idcontact, String idchat, String fecha) {
        this.setIdcreador(idcreador);
        this.setNamechat(namechat);
        this.setFecha(fecha);
        this.setIdcontact(idcontact);
        this.setIdchat(idchat);
    }


}
