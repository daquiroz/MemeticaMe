package com.mobility42.azurechatr;

import java.util.*;

public class Contact {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getContactphoto() {
        return contactphoto;
    }

    public void setContactphoto(String contactphoto) {
        this.contactphoto = contactphoto;
    }

    @com.google.gson.annotations.SerializedName("name")
    private String name;

    @com.google.gson.annotations.SerializedName("mail")
    private String mail;

    @com.google.gson.annotations.SerializedName("id")
    private String id;

    @com.google.gson.annotations.SerializedName("estado")
    private String estado;

    @com.google.gson.annotations.SerializedName("contactphoto")
    private String contactphoto;

    public Contact(String name, String mail, String id, String estado, String contactphoto) {
        this.name = name;
        this.mail = mail;
        this.id = id;
        this.estado = estado;
        this.contactphoto = contactphoto;
    }

    public Contact() {

    }




}
