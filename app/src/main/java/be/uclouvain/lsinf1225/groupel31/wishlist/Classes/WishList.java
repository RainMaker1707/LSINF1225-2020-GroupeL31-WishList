package be.uclouvain.lsinf1225.groupel31.wishlist.Classes;

import android.media.Image;

import java.sql.Blob;
import java.util.List;

public class WishList {

    private String name;
    private Image picture;
    private Integer size;
    private String owner;
    private List<Wish> wishLst;

    public WishList(String name, Image picture, Integer size, String owner){
        this.name = name;
        this.picture = picture;
        this.size = size;
        this.owner = owner;
    }

    public String getName() {
        return this.name;
    }

    public Image getPicture() {
        return this.picture;
    }

    public Integer getSize() {
        return this.size;
    }

    public String getOwner(){
        return this.owner;
    }

    public List<Wish> getWishLst(){
        return this.wishLst;
    }

    public void setOwner(String owner){
        this.owner = owner;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPicture(Image picture) {
        this.picture = picture;
    }

    public boolean addWish(Wish wish){
        return false;
    }

    public boolean deleteWish(Wish wish){
        return false;
    }

    public boolean addPermission(String mail, int permId){
        return false;
    }

    public boolean deletePermission(String mail, int permId) {
        return false;
    }

    public boolean canRead(String mail){
        return false;
    }

    public boolean canWrite(String mail){
        return false;
    }
}
