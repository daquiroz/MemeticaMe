package com.example.dani.memeticame.model;

/**
 * Created by Dani on 26-10-15.
 */
public class Mensaje {

    private String text;
    private String status;
    private String idsender;
    private String idreceiver;
    private String idchat;

    public Mensaje(String text, String status, String idsender, String idreceiver, String idchat) {
        this.text = text;
        this.status = status;
        this.idsender = idsender;
        this.idreceiver = idreceiver;
        this.idchat = idchat;
    }

    public String getIdchat() {
        return idchat;
    }

    public void setIdchat(String idchat) {
        this.idchat = idchat;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdsender() {
        return idsender;
    }

    public void setIdsender(String idsender) {
        this.idsender = idsender;
    }

    public String getIdreceiver() {
        return idreceiver;
    }

    public void setIdreceiver(String idreceiver) {
        this.idreceiver = idreceiver;
    }


}
