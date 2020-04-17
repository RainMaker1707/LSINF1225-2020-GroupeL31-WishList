package be.uclouvain.lsinf1225.groupel31.wishlist.tools;

import android.content.Context;
import android.database.Cursor;
import android.media.Image;

import androidx.annotation.Nullable;

public class User {
    private String email;
    private String pseudo;
    private String password;
    private String address;
    private Image profilPicture;
    private AccessDataBase db;

    public User(String mail, String password, String pseudo, Context context){
        db = new AccessDataBase(context);
        this.email = mail;
        this.pseudo = pseudo;
        this.password = password;

    }

    public boolean ExistingUSer(String email){
        String req = "SELECT * FROM user WHERE mail=\"" + email + "\";";
        Cursor cursor = db.select(req);
        return true;
    }

    public void signIn(String mail, String password){
        String result;
    }

    public void signUp( String email, String pseudo, String password,
                       @Nullable String address, @Nullable String profilPicture){
        String req = "INSERT INTO User (pseudo, password, mail, photo, address) VALUES ";
        req += "(\"" + pseudo + "\", \"" + password + "\", \""+ email;
        req += "\", \"" + profilPicture + "\", \"" + address + "\");";
        db.insert(req);
    }
}
