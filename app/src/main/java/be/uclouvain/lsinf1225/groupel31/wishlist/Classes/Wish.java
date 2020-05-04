package be.uclouvain.lsinf1225.groupel31.wishlist.Classes;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.media.Image;

import androidx.annotation.Nullable;

import be.uclouvain.lsinf1225.groupel31.wishlist.tools.AccessDataBase;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.ImageToBlob;

public class Wish {
    private String name;
    private String market;
    private Integer id;
    private String owner;
    private Bitmap picture;
    private String description;
    private double price;
    private double eval;

    /** Constructor
     * @param name
     * @param owner
     * @param description
     * @param price
     * @param market
     */
    public Wish(int id, String name, String owner, String description, double price, String market) {
        this.id = id;
        this.name = name;
        this.owner = owner;
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

    public void setPicture(Bitmap picture) {
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

    public Bitmap getPicture() {
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
