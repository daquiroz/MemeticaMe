package com.mobility42.azurechatr;

/**
 * Created by Dani on 07-12-15.
 */
public class Meme {



    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getRanking() {
        return ranking;
    }

    public void setRanking(int likes) {
        this.ranking = ranking;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(String etiquetas) {
        this.etiquetas = etiquetas;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getIdcanal() {
        return idcanal;
    }

    public void setIdcanal(String idcanal) {
        this.idcanal = idcanal;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getNombrecanal() {
        return nombrecanal;
    }

    public void setNombrecanal(String nombrecanal) {
        this.nombrecanal = nombrecanal;
    }


    @com.google.gson.annotations.SerializedName("url")
    private String url;

    @com.google.gson.annotations.SerializedName("ranking")
    private String ranking;

    @com.google.gson.annotations.SerializedName("imagepath")
    private String imagePath;

    @com.google.gson.annotations.SerializedName("etiquetas")
    private String etiquetas;

    @com.google.gson.annotations.SerializedName("categoria")
    private String categoria;

<<<<<<< HEAD
=======
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @com.google.gson.annotations.SerializedName("id")
    private String id;


    public Meme(String url, String ranking, String etiquetas, String categoria, String idcanal, String nombrecanal, String id) {
        this.url = url;
        this.ranking = ranking;
        this.etiquetas = etiquetas;
        this.categoria = categoria;
        this.idcanal = idcanal;
        this.nombrecanal = nombrecanal;
        this.id = id;
    }

    @com.google.gson.annotations.SerializedName("idcanal")
    private String idcanal;

    @com.google.gson.annotations.SerializedName("nombrecanal")
    private String nombrecanal;
>>>>>>> Daniela

}
