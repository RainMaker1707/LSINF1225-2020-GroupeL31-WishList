package be.uclouvain.lsinf1225.groupel31.wishlist.Classes;

import android.content.Context;
import android.database.Cursor;
import android.media.Image;

import androidx.annotation.Nullable;

import be.uclouvain.lsinf1225.groupel31.wishlist.tools.AccessDataBase;

public class User {
    private boolean created = false;
    private String email;
    private String pseudo;
    private String password;
    private String address;
    private Image profilePicture;
    private AccessDataBase db;

    private static User user = new User();

    private User(){
        this.created = true;
    }

    public static User getInstance(){
        return user;
    }
    public void signIn(String mail, String password, Context context){
        this.email = mail;
        this.password = password;
        this.db = new AccessDataBase(context);
    }

    private void LogOut(){
        this.created = false;
        this.email = null;
        this.pseudo = null;
        this.password = null;
        this.address = null;
        this.profilePicture = null;
        this.db = null;

    }

    public Cursor ExistingUSer(String email){
        String req = "SELECT * FROM user WHERE mail=\"" + email + "\";";
        return db.select(req);
    }

    public void signUp( String email, String pseudo, String password,
                       @Nullable String address, @Nullable String profilePicture){
        String req = "INSERT INTO User (pseudo, password, mail, photo, address) VALUES ";
        req += "(\"" + pseudo + "\", \"" + password + "\", \""+ email;
        req += "\", \"" + profilePicture + "\", \"" + address + "\");";
        db.insert(req);
    }

    public void createWishList(String name, @Nullable Image picture){

    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfilePicture(Image profilePicture) {
        this.profilePicture = profilePicture;
    }
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getEmail() {
        return email;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public Image getProfilePicture() {
        return profilePicture;
    }

}
