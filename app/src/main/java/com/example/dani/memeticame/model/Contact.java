package com.example.dani.memeticame.model;

/**
 * Created by Dani on 26-10-15.
 */
public class Contact {


    private String name;
    private String mail;
    private String estado;
    private String contactphoto;

    public String getContactphoto() {
        return contactphoto;
    }

    public void setContactphoto(String contactphoto) {
        this.contactphoto = contactphoto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
