package be.uclouvain.lsinf1225.groupel31.wishlist.Classes;

import android.media.Image;

public class Wish {
    private String name;
    private String market;
    private Integer id;
    private Image picture;
    private String description;
    private double price;
    private double eval;

    // Constructor
    public Wish(String name, Image picture, String description, double price, String market) {
        this.name = name;
        this.picture = picture;
        this.description = description;
        this.price = price;
        this.market = market;
    }


    // ******* Getters and setters ******
    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPicture(Image picture) {
        this.picture = picture;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public void setEval(double eval) {
        this.eval = eval;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public Image getPicture() {
        return picture;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getMarket() {
        return market;
    }

    public double getEval() {
        return eval;
    }
}
