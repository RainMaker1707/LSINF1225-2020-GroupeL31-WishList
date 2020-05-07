package be.uclouvain.lsinf1225.groupel31.wishlist.Classes;

import android.graphics.Bitmap;

public class Wish {
    private String name;
    private String market;
    private Integer id;
    private Bitmap picture;
    private String description;
    private double price;

    /** Constructor
     * @param name wish's name
     * @param description wish's description
     * @param price wish's price
     * @param market wish's market where you can find it
     */
    public Wish(int id, String name, String description, double price, String market) {
        this.id = id;
        this.name = name;
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

}
