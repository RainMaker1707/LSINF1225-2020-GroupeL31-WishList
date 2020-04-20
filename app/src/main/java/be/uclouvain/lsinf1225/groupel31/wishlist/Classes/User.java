package be.uclouvain.lsinf1225.groupel31.wishlist.Classes;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.media.Image;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.lsinf1225.groupel31.wishlist.tools.AccessDataBase;

public class User {
    private boolean created;
    private boolean signIn = false;
    private String email;
    private String pseudo;
    private String password;
    private String address;
    private Image profilePicture;
    private List<WishList> wishlist_list;
    private AccessDataBase db;

    private static User user = new User();

    private User(){
        this.created = true;
    }

    public static User getInstance(){
        return user;
    }


    public void signIn(String mail){
        this.signIn = true;
        setRefFromDb(mail);
    }

    private void setRefFromDb(String mail) {
        Cursor cursor = db.select("SELECT * FROM user WHERE mail=\"" + mail + "\";");
        cursor.moveToLast();
        setPseudo(cursor.getString(0));
        setPassword(cursor.getString(1));
        setEmail(cursor.getString( 2));
        setProfilePicture(null);//TODO set with cursor.getBlob(3) bitsmap
        setAddress(cursor.getString(4));
        cursor.close();

        List<WishList> wishlists= new ArrayList<>();
        cursor = db.select("SELECT * FROM WishList WHERE owner=\"" + getEmail() + "\";");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            wishlists.add(new WishList(this.db, cursor.getInt(0), cursor.getString(1),
                    null, cursor.getInt(4), cursor.getString(2)));
            cursor.moveToNext();
        }
        cursor.close();
        setWishlist_list(wishlists);
    }

    private void updateWishList(){
        List<WishList> wishlists= new ArrayList<>();
        Cursor cursor = db.select("SELECT * FROM WishList WHERE owner=\""+ getEmail() + "\";");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            wishlists.add(new WishList(this.db, cursor.getInt(0), cursor.getString(1),
                    null, cursor.getInt(4), cursor.getString(2)));
            cursor.moveToNext();
        }
        cursor.close();
        setWishlist_list(wishlists);
    }

    public void LogOut(){
        setCreated(false);
        setSignIn(false);
        setAddress(null);
        setEmail(null);
        setPassword(null);
        setProfilePicture(null);
        setPseudo(null);
        setWishlist_list(null);
        setDb(null);

    }

    public boolean matchingPassAndMail(String password, String mail){
        return this.getPassword().equals(password) && this.getEmail().equals(mail);
    }

    public boolean ExistingUSer(String email){
        String req = "SELECT * FROM user WHERE mail=\"" + email + "\";";
        Cursor cursor = db.select(req);
        try {
            cursor.moveToLast();
            cursor.getString(1);
        }catch(CursorIndexOutOfBoundsException e){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void signUp( String email, String pseudo, String password,
                       @Nullable String address, @Nullable String profilePicture){
        String req = "INSERT INTO User (pseudo, password, mail, photo, address) VALUES ";
        req += "(\"" + pseudo + "\", \"" + password + "\", \""+ email;
        req += "\", \"" + profilePicture + "\", \"" + address + "\");";
        db.insert(req);
    }

    public void createWishList(String name, @Nullable Image picture){
        String req = "INSERT INTO Wishlist (name, owner, picture) VALUES ";
        req += "(\"" + name + "\", \"" + this.getEmail() + "\", \"" + picture + "\");";
        db.insert(req);
        this.updateWishList();
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

    public List<WishList> getWishlist_list() {
        return this.wishlist_list;
    }

    public boolean isSignIn() {
        return signIn;
    }

    public void setWishlist_list(List<WishList> wishlist_list) {
        this.wishlist_list = wishlist_list;
    }

    public void setSignIn(boolean signIn) {
        this.signIn = signIn;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public void setDb(Context context) {
        this.db = new AccessDataBase(context);
    }
}
