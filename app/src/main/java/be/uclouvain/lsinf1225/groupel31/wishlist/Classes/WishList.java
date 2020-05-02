package be.uclouvain.lsinf1225.groupel31.wishlist.Classes;

import android.content.Context;
import android.database.Cursor;
import android.media.Image;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.lsinf1225.groupel31.wishlist.tools.AccessDataBase;

public class WishList {

    private String name;
    private Image picture;
    private Integer size;
    private String owner;
    private Integer id;
    private AccessDataBase db;
    private List<Wish> wishLst = new ArrayList<>();

    /** Constructor
     * @param db
     * @param id
     * @param name
     * @param picture
     * @param size
     * @param owner
     */
    public WishList(AccessDataBase db, Integer id, String name, Image picture, Integer size, String owner){
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.size = size;
        this.owner = owner;
        this.db = db;
        updateWishLst();
    }

    /** Create a list of wish from the db data
     */
    private void updateWishLst() {
        //Select
        String req = "SELECT W.* FROM Wishlist L, Wish W, Content C WHERE C.wishlist=\"" +
                this.getId() + "\" AND C.product = W.num GROUP BY W.num;";
        Cursor cursor = db.select(req);
        List<Wish> wishes = new ArrayList<>();
        cursor.moveToFirst();
        //loop to append wish in list
        while(!cursor.isAfterLast()){
            wishes.add(new Wish(cursor.getString(1), null, cursor.getString(4),
                    cursor.getDouble(5), cursor.getString(6)));
            cursor.moveToNext();
        }
        setWishLst(wishes);
        cursor.close();
    }

    /** insert a new line in db table wish
     * @param name
     * @param picture
     * @param description
     * @param price
     * @param market
     */
    public void createWish(String name, Image picture, String description,
                           double price, String market){
        // insert
        String req = "INSERT INTO Wish (name, photo, wish_id, desc, prix, market) VALUES ";
        req += "(\"" + name + "\", \"" + picture + "\", \"" + this.id + "\", \"";
        req += description + "\", \"" + price + "\", \"" + market + "\");";
        db.insert(req);

        //select to find id
        Cursor cursor = db.select("SELECT * FROM Wish");
        cursor.moveToLast();
        int wish_id = cursor.getInt(0); // get id
        cursor.close();

        //insert link wishlist-wish in db table content
        req = "INSERT INTO Content (wishlist, product) VALUES (\"" + this.id + "\", \"" + wish_id + "\");";
        db.insert(req);
        updateWishLst();
        this.size++;
        db.insert("UPDATE Wishlist SET size=" + this.size + " WHERE id=" + this.getId());
    }

    public void changeName(String input) {
        db.insert("UPDATE Wishlist SET name=\"" + input + "\" WHERE id=" +this.getId());
        this.setName(input);
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


    // ***** Getters and setters *****
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setWishLst(List<Wish> wishLst) {
        this.wishLst = wishLst;
    }

}
