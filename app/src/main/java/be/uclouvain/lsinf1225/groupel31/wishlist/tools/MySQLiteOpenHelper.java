package be.uclouvain.lsinf1225.groupel31.wishlist.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    /**
     *
     * @param context application context
     * @param name name of the database
     * @param factory factory?
     * @param version index of data base version
     */
    MySQLiteOpenHelper(@Nullable Context context, @Nullable String name,
                       @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Create table if doesn't exist in data base (user only for the moment)
     * @param db sqlite data base
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE User ("
                +" pseudo   VARCHAR (255) NOT NULL,"
                +" password VARCHAR (255) NOT NULL,"
                +" mail     VARCHAR (255) PRIMARY KEY NOT NULL UNIQUE,"
                +" photo    BLOB,"
                +" address  VARCHAR (255));"
        );
        db.execSQL("CREATE TABLE Wishlist ("
                +"id       INTEGER       PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE,"
                +"name     VARCHAR (255) NOT NULL,"
                +"owner    VARCHAR (255) REFERENCES User (mail) NOT NULL,"
                +"picture  BLOB,"
                +"size     INT           DEFAULT (0),"
                +"[update] DATETIME);"
        );
        db.execSQL("CREATE TABLE Wish ("
                +"num    INTEGER       PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE,"
                +"name   VARCHAR (255) NOT NULL,"
                +"photo  BLOB          NOT NULL,"
                +"[desc] TEXT (5000),"
                +"prix DOUBLE NOT NULL,"
                +"market VARCHAR (255),"
                +"eval DOUBLE DEFAULT (0));"
        );
        db.execSQL("CREATE TABLE Perm ("
                +"mail VARCHAR (255) REFERENCES User (mail) NOT NULL,"
                +"perm INTEGER       CHECK (0 <= perm < 3) NOT NULL DEFAULT (2),"
                +"id   INTEGER       REFERENCES Wishlist (id)NOT NULL,"
                +"PRIMARY KEY (mail, id));"
        );
        db.execSQL("CREATE TABLE Interest ("
                +"mail VARCHAR (255) REFERENCES User (mail) NOT NULL,"
                +"id   INTEGER       REFERENCES Wishlist (id) NOT NULL,"
                +"PRIMARY KEY (mail, id));"
        );
        db.execSQL("CREATE TABLE Eval ("
                +"mail VARCHAR (255) REFERENCES User (mail) NOT NULL,"
                +"id   INTEGER       REFERENCES Wishlist (id) NOT NULL,"
                +"num  INTEGER       REFERENCES Wish (num) NOT NULL,"
                +"eval INTEGER       CHECK (0 <= eval < 11) NOT NULL,"
                +"PRIMARY KEY (mail, id, num));"
        );
        db.execSQL("CREATE TABLE Content ("
                +"wishlist INTEGER REFERENCES Wishlist (id),"
                +"product  INTEGER REFERENCES Wish (num));"
        );
    }

    /**
     * @param db sqlite data base
     * @param oldVersion index of old version
     * @param newVersion index of new version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
