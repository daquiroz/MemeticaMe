package com.example.dani.memeticame.model;

/**
 * Created by Dani on 26-10-15.
 */
public class Chat {

    private String namechat;
    private String idcreador;
    private String idcontact;
    private String idchat;
    private String fecha;

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdchat() {
        return idchat;
    }

    public void setIdchat(String idchat) {
        this.idchat = idchat;
    }

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

    public String getIdcontact() {
        return idcontact;
    }

    public void setIdcontact(String idcontact) {
        this.idcontact = idcontact;
    }
}
