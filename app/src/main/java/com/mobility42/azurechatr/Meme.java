package com.mobility42.azurechatr;

/**
 * Created by Dani on 07-12-15.
 */
public class Meme {

    @com.google.gson.annotations.SerializedName("url")
    private String url;

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

    @com.google.gson.annotations.SerializedName("likes")
    private String ranking;

    @com.google.gson.annotations.SerializedName("etiquetas")
    private String etiquetas;

    @com.google.gson.annotations.SerializedName("categoria")
    private String categoria;
}
