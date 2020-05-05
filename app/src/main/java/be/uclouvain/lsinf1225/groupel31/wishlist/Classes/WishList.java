package be.uclouvain.lsinf1225.groupel31.wishlist.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.lsinf1225.groupel31.wishlist.tools.AccessDataBase;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.ImageToBlob;

public class WishList {

    private String name;
    private Bitmap picture;
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
    public WishList(AccessDataBase db, Integer id, String name, Bitmap picture, Integer size, String owner){
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
            Wish toAdd = new Wish(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(3), cursor.getString(4),
                    cursor.getDouble(5), cursor.getString(6));
            if(cursor.getBlob(2) != null){
                toAdd.setPicture(ImageToBlob.getBytePhoto(cursor.getBlob(2)));
            }
            wishes.add(toAdd);
            cursor.moveToNext();
        }
        setWishLst(wishes);
        cursor.close();

        this.size = wishes.size();
    }

    /** insert a new line in db table wish
     * @param name
     * @param picture
     * @param description
     * @param price
     * @param market
     */
    public void createWish(String name, Bitmap picture, String description,
                           double price, String market){
        // insert
        String req = "INSERT INTO Wish (name, photo, wish_id, desc, prix, market) VALUES ";
        req += "(\"" + name + "\", \"" + picture + "\", \"" + this.id + "\", \"";
        req += description + "\", \"" + price + "\", \"" + market + "\");";
        db.insert(req);

        // select to find id of last wish inserted
        Cursor cursor = db.select("SELECT * FROM Wish");
        cursor.moveToLast();
        int wish_id = cursor.getInt(0); // get id
        cursor.close();

        // update picture
        ContentValues values = new ContentValues();
        values.put("photo", ImageToBlob.getBytes(picture));
        String selection = "num LIKE ?";
        String[] selectionArg = {String.format("%s", wish_id)};
        db.get().update("Wish", values, selection, selectionArg);

        // link wish
        this.linkWish(wish_id);
    }

    public void linkWish(int wish_id){
        //insert link wishlist-wish in db table content
        String req = "INSERT INTO Content (wishlist, product) VALUES (\"" + this.id + "\", \"" + wish_id + "\");";
        db.insert(req);
        this.size++;
        db.insert("UPDATE Wishlist SET size=" + this.size + " WHERE id=" + this.getId());
        updateWishLst();
    }

    public void changeName(String input) {
        db.insert("UPDATE Wishlist SET name=\"" + input + "\" WHERE id=" +this.getId());
        this.setName(input);
    }

    public void updatePicture(Bitmap picture){
        if(picture == null){
            this.setPicture(null);
        }else {
            ContentValues values = new ContentValues();
            values.put("picture", ImageToBlob.getBytes(picture));
            String selection = "id LIKE ?";
            String[] selectionArg = {String.format("%s", this.getId())};
            db.get().update("Wishlist", values, selection, selectionArg);
            this.setPicture(picture);
        }
    }

    public void unlinkWish(int wish_id){
        String req = "DELETE FROM Content WHERE wishlist=\""+
                this.getId() +"\" AND product=\""+wish_id +"\";";
        db.insert(req);
        this.updateWishLst();
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

    public Bitmap getPicture() {
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

    public void setPicture(Bitmap picture) {
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
