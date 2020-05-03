package be.uclouvain.lsinf1225.groupel31.wishlist.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.media.Image;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentUser;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.AccessDataBase;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.ImageToBlob;

public class User {

    private String email;
    private String pseudo;
    private String password;
    private String address;
    private Context context;
    private String sport;
    private String meal;
    private String color;
    private String hobby;
    private Bitmap profilePicture;
    private List<WishList> wishlist_list;
    private List<User> friendList;
    private AccessDataBase db;
    private boolean isFriend = false;
    private boolean requested = false;

    //only set the created on true
    public User(){

    }

    /** Set signIn on true, retrieve all data from data base and set the current user singleton
     * on this one
     */
    public void signIn(String mail){
        setEmail(mail);
        CurrentUser.setInstance(this);
        setRefFromDb(mail);
    }

    /** Retrieve all data from db and set it in usable var
     */
    public void setRefFromDb(String mail) {
        Cursor cursor = db.select("SELECT * FROM user WHERE mail=\"" + mail + "\";");
        cursor.moveToLast();
        setPseudo(cursor.getString(0));
        setPassword(cursor.getString(1));
        setEmail(cursor.getString( 2));
        if(cursor.getBlob(3) != null) {
            byte[] picture = cursor.getBlob(3);
            setProfilePicture(ImageToBlob.getBytePhoto(picture));
        }
        setSport(cursor.getString(4));
        setfavColor(cursor.getString(5));
        setHobby(cursor.getString(6));
        setMeal(cursor.getString(7));
        setAddress(cursor.getString(8));
        cursor.close();
        updateWishList();
        if(CurrentUser.getInstance().getEmail().equals(mail)) {
            updateFriendList();
        }

    }


    public void addMoreInfo(String address, String color, String meal, String sport, String hobby){
        db.insert("UPDATE User SET address=\"" + address + "\", color=\"" + color +"\", "
                +"meal=\"" + meal + "\", sport=\"" + sport + "\", hobby=\"" + hobby + "\" "
                +"WHERE mail=\"" + this.getEmail() + "\";");
        this.setRefFromDb(this.getEmail());
    }

    /** Update the wishlist from the database
     */
    private void updateWishList(){
        List<WishList> wishlists= new ArrayList<>();
        Cursor cursor = db.select("SELECT * FROM WishList WHERE owner=\""+ getEmail() + "\";");
        cursor.moveToFirst();
        //loop to append wishlist in list
        while(!cursor.isAfterLast()){
            wishlists.add(new WishList(this.db, cursor.getInt(0), cursor.getString(1),
                    null, cursor.getInt(4), cursor.getString(2)));
            cursor.moveToNext();
        }
        cursor.close();
        setWishlist_list(wishlists);
    }

    /** Update the friend list from the dataBase
     */
    private void updateFriendList(){
        List<User> friendList = new ArrayList<>();
        Cursor cursor = db.select("SELECT mail_host, relation, mail_requested " +
                "FROM Friend WHERE (mail_host=\""+ this.getEmail() +"\" " +
                "OR mail_requested=\""+ this.getEmail() + "\");");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            String temp_mail = cursor.getString(0);
            if(temp_mail.equals(CurrentUser.getInstance().getEmail())){
                temp_mail = cursor.getString(2);
            }

            User toAdd = new User();
            toAdd.setDb(this.context);
            toAdd.setRefFromDb(temp_mail);
            if(cursor.getInt(1) == 1 ){toAdd.setFriend(true);}
            else if(temp_mail.equals(cursor.getString(0))){toAdd.setRequested(true);}
            friendList.add(toAdd);
            cursor.moveToNext();
        }
        cursor.close();
        setFriendList(friendList);
    }

    public void addFriend(String mail){
        db.insert("INSERT INTO Friend (mail_host, relation, mail_requested)"
                + "VALUES (\""+ this.getEmail() + "\", 0, \""
                + mail + "\");");
        updateFriendList();
    }

    public void acceptRequest(String mail){
        db.insert("UPDATE Friend SET relation=1 " +
                "WHERE (mail_host=\"" + this.getEmail() + "\" AND mail_requested=\"" + mail +"\") " +
                "OR (mail_host=\"" + mail + "\" AND mail_requested=\"" + this.getEmail()+ "\");");
        updateFriendList();
    }

    public void refuseRequest(String mail){
        deleteFriend(mail);
    }

    public void deleteFriend(String mail){
        db.insert("DELETE FROM Friend WHERE (mail_host=\""
                + this.getEmail() + "\" AND mail_requested=\""
                + mail + "\") OR (mail_host=\"" + mail + "\" AND mail_requested=\""
                + this.getEmail() + "\");");
        updateFriendList();
    }

    /** Destroy the user attributes and remove the singleton reference
     */
    public void LogOut(){
        destroyUser();
        CurrentUser.setInstance(null);
        Toast.makeText(this.context, "See You Soon !",
                Toast.LENGTH_SHORT).show();
    }

    /** remove all user's attributes
     */
    private void destroyUser(){
        setAddress(null);
        setEmail(null);
        setPassword(null);
        this.profilePicture = null;
        setPseudo(null);
        setWishlist_list(null);
        setMeal(null);
        setfavColor(null);
        setSport(null);
        setHobby(null);
    }

    /** Function which just check if password and mail passed as arg are same as these stored in db
     * @param password user's password input
     * @param mail user's mail input
     * @return true if matches false else
     */
    public boolean matchingPassAndMail(String password, String mail){
        return this.getPassword().equals(password) && this.getEmail().equals(mail);
    }

    /** Check if a line with the email apssed as arg exist in db
     * @param email user's mail input
     * @return true if exist false else
     */
    public boolean ExistingUSer(String email){
        String req = "SELECT * FROM user WHERE mail=\"" + email + "\";";
        Cursor cursor = db.select(req);
        try {
            cursor.moveToLast();
            cursor.getString(1);
        }catch(CursorIndexOutOfBoundsException e){ // if cursor return none line
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    /** Store a new line in db table user
     * @param email user's mail input
     * @param pseudo user's pseudo input
     * @param password user's password input
     * @param address user's address input
     * @param profilePicture user's profile input
     */
    public void signUp( String email, String pseudo, String password,
                       @Nullable String address, @Nullable String profilePicture){
        String req = "INSERT INTO User (pseudo, password, mail, photo, address) VALUES ";
        req += "(\"" + pseudo + "\", \"" + password + "\", \""+ email;
        req += "\", \"" + profilePicture + "\", \"" + address + "\");";
        db.insert(req);
    }

    /** Store a new line in db table wishlist
     * @param name name of the new wishlist
     * @param picture picture for the new wishlist
     */
    public void createWishList(String name, @Nullable Image picture){
        String req = "INSERT INTO Wishlist (name, owner, picture) VALUES ";
        req += "(\"" + name + "\", \"" + this.getEmail() + "\", \"" + picture + "\");";
        db.insert(req);
        this.updateWishList(); // to update the object linked with the wishlist
    }

    /** Delete a line in the db's table WishList
     * @param id wishlist's id to delete
     */
    public void deleteWishList(int id){
        String req = "DELETE FROM Wishlist WHERE id=" + id + ";";
        db.insert(req);
        updateWishList();
    }

    public void updateProfilePicture(Bitmap profilePicture){
        ContentValues values = new ContentValues();
        values.put("photo", ImageToBlob.getBytes(profilePicture));
        String selection = "mail LIKE ?";
        String[] selectionArg = {this.getEmail()};
        db.get().update("User", values, selection, selectionArg);
        this.setRefFromDb(this.getEmail());
    }

    // ******* getters and setters *****
    private void setEmail(String email){
        this.email = email;
    }

    private void setAddress(String address) {
        this.address = address;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    private void setProfilePicture(Bitmap bytePhoto) {
        this.profilePicture = bytePhoto;
    }

    private void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    private void setFriendList(List<User> users) {
        this.friendList = users;
    }

    public List<User> getFriendList(){
        updateFriendList();
        return this.friendList;
    }

    public String getEmail() {
        return email;
    }

    public String getPseudo() {
        return pseudo;
    }

    private String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    public List<WishList> getWishlist_list() {
        return this.wishlist_list;
    }

    private void setWishlist_list(List<WishList> wishlist_list) {
        this.wishlist_list = wishlist_list;
    }


    public void setDb(Context context) {
        this.context = context;
        this.db = new AccessDataBase(context);
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public boolean isRequested() {
        return requested;
    }

    public void setRequested(boolean requested) {
        this.requested = requested;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getFavColor() {
        return color;
    }

    public void setfavColor(String color) {
        this.color = color;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }
}
