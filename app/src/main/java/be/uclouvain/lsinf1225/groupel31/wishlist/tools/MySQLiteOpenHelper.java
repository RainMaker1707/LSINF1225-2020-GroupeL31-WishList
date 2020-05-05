package be.uclouvain.lsinf1225.groupel31.wishlist.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.User;
import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.Wish;
import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.WishList;

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
                +" sport    VARCHAR,"
                +" color VARCHAR,"
                +" hobby VARCHAR,"
                +" meal VARCHAR,"
                +" address  VARCHAR (255));"
        );

        db.execSQL("CREATE TABLE Friend (\n"
                +"mail_host      VARCHAR (255) REFERENCES User (mail) NOT NULL,"
                +"relation       BOOLEAN       NOT NULL DEFAULT (0),"
                +"mail_requested VARCHAR (255) REFERENCES User (mail) NOT NULL,"
                +"PRIMARY KEY (mail_host, mail_requested ));"
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
                +"photo  BLOB,"
                +"[desc] TEXT (5000),"
                +"price DOUBLE NOT NULL,"
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

        //fill db at installation
        String default_passord = "wishlist";
        String req = "INSERT INTO User (pseudo, password, mail, sport, color, hobby, meal, address) VALUES ";

        //user 1 & some wishlists
        db.execSQL(req + "(\"Hadrien\", \"" + default_passord + "\", \"hadrien@gmail.com\"," +
                " \"ThaiBoxe\", \"Blue\", \"Dev\", \"Steak\", \"Fleurus\");");
        db.execSQL("INSERT INTO Wishlist (name, owner) VALUES (\"Christmas\", \"hadrien@gmail.com\");");
        db.execSQL("INSERT INTO Wishlist (name, owner) VALUES (\"Birthday\", \"hadrien@gmail.com\");");
        db.execSQL("INSERT INTO Wishlist (name, owner) VALUES (\"Tesla\", \"hadrien@gmail.com\");");
        db.execSQL("INSERT INTO Wishlist (name, owner) VALUES (\"Gaming\", \"hadrien@gmail.com\");");

        //user 2 & some wishlists
        db.execSQL(req + "(\"Nicolas\", \"" + default_passord + "\", \"nicolas@gmail.com\"," +
                " \"Natation\", \"Red\", \"Dev\", \"Salad\", \"Courcelles\");");
        db.execSQL("INSERT INTO Wishlist (name, owner) VALUES (\"Natation\", \"nicolas@gmail.com\");");
        db.execSQL("INSERT INTO Wishlist (name, owner) VALUES (\"Computer\", \"nicolas@gmail.com\");");
        db.execSQL("INSERT INTO Wishlist (name, owner) VALUES (\"Servers\", \"nicolas@gmail.com\");");

        //user 3 & some wishlists
        db.execSQL(req + "(\"Julien\", \"" + default_passord + "\", \"julien@gmail.com\"," +
                " \"Foot\", \"Orange\", \"Dev\", \"Pasta\", \"Louvain\");");
        db.execSQL("INSERT INTO Wishlist (name, owner) VALUES (\"Tesla\", \"julien@gmail.com\");");
        db.execSQL("INSERT INTO Wishlist (name, owner) VALUES (\"Foot\", \"julien@gmail.com\");");

        //user 4 & some wishlists
        db.execSQL(req + "(\"Noemie\", \"" + default_passord + "\", \"noemie@gmail.com\"," +
                " \"Dance\", \"Green\", \"Sewing\", \"Steak\", \"Farciennes\");");
        db.execSQL("INSERT INTO Wishlist (name, owner) VALUES (\"Christmas\", \"noemie@gmail.com\");");
        db.execSQL("INSERT INTO Wishlist (name, owner) VALUES (\"Birthday\", \"noemie@gmail.com\");");
        db.execSQL("INSERT INTO Wishlist (name, owner) VALUES (\"Sewing\", \"noemie@gmail.com\");");

        //user 5 & some wishlists
        db.execSQL(req + "(\"Francois\", \"" + default_passord + "\", \"francois@gmail.com\"," +
                " \"NoSport\", \"Black\", \"Garden\", \"Pizza\", \"Pont-de-loup\");");
        db.execSQL("INSERT INTO Wishlist (name, owner) VALUES (\"Garden\", \"francois@gmail.com\");");
        db.execSQL("INSERT INTO Wishlist (name, owner) VALUES (\"Rabbit\", \"francois@gmail.com\");");
        db.execSQL("INSERT INTO Wishlist (name, owner) VALUES (\"Baby\", \"francois@gmail.com\");");

        //user 6 & some wishlists
        db.execSQL(req + "(\"Shaparder\", \"" + default_passord + "\", \"shaparder@gmail.com\"," +
                " \"Hockey\", \"Blue\", \"Read\", \"Steak\", \"Bruxelles\");");
        db.execSQL("INSERT INTO Wishlist (name, owner) VALUES (\"Tesla\", \"shaparder@gmail.com\");");
        db.execSQL("INSERT INTO Wishlist (name, owner) VALUES (\"Gaming\", \"shaparder@gmail.com\");");

        //user 7 & some wishlists
        db.execSQL(req + "(\"Stanley\", \"" + default_passord + "\", \"stanley@gmail.com\"," +
                " \"Badminton\", \"Yellow\", \"Stat\", \"Asiat\", \"Brugges\");");
        db.execSQL("INSERT INTO Wishlist (name, owner) VALUES (\"Birthday\", \"stanley@gmail.com\");");

        //user 8 & some wishlists
        db.execSQL(req + "(\"Alexandre\", \"" + default_passord + "\", \"alexandre@gmail.com\"," +
                " \"Running\", \"Purple\", \"Read\", \"Chiness\", \"Gembloux\");");
        db.execSQL("INSERT INTO Wishlist (name, owner) VALUES (\"Christmas\", \"alexandre@gmail.com\");");
        db.execSQL("INSERT INTO Wishlist (name, owner) VALUES (\"Birthday\", \"alexandre@gmail.com\");");

        //create some product
        req = "INSERT INTO Wish (name, desc, price, market) VALUES ";
        db.execSQL(req + "(\"PS4\", \"Playsattion with 1TB HardDriveDisk\", 95.99, \"Amazon\");");
        db.execSQL(req + "(\"MacBook\", \"MacBook Pro 2016 258GB 4core, 3.1GHz\", 545.54, \"Amazon\");");
        db.execSQL(req + "(\"Polo\", \"SpaceX polo with tag\", 50.00, \"SpaceX shop\");");
        db.execSQL(req + "(\"GreenHouse\", \"A little GreenHouse to made my garden a better area\", " +
                "550.55, \"Brico Depot\");");
        db.execSQL(req + "(\"COD\", \"call of duty last ep \", 59.99, \"Steam Store\");");
        db.execSQL(req + "(\"Fifa\", \"World known game\", 50.99, \"Playstation Store\");");
        db.execSQL(req + "(\"LearnGit\", \"Book to learn how use git\", 20, \"Amazon\");");//for stanley
        db.execSQL(req + "(\"Tesla\", \"Tesla modèle X too cheap so I decided to made the price at " +
                "1euro to don't hurt you \", 1.00, \"Tesla Store\");");
        db.execSQL(req + "(\"Guitar\", \"Simple guitar to learn how to play with\", 145.00, \"Amazon\");");
        db.execSQL(req + "(\"Bed\", \"Cage bed with barrel for baby\", 120, \"Dreamland\");");
        db.execSQL(req + "(\"Tutu\", \"Tutu for baby chewing\", 10, \"Dreamland\");");
        db.execSQL(req + "(\"Turkey\", \"maxi turkey 12kg for christmas\", 50, \"Butcher shop\");");
        db.execSQL(req + "(\"Cage\", \"cage for rabbits\", 70, \"Brico\");");
        db.execSQL(req + "(\"Fence\", \"garden wood fences\", 110, \"Brico\");");
        db.execSQL(req + "(\"SewingKit\", \"to make mask for the deconfinement\", 25, \"Amazon\");");

        //set picture profile
        String[] tab = {"hadrien@gmail.com", "nicolas@gmail.com", "julien@gmail.com",
                        "noemie@gmail.com", "francois@gmail.com", "shaparder@gmail.com",
                        "stanley@gmail.com", "alexandre@gmail.com"};
        User user = new User();
        for(int i = 1; i < 9; i++) {
            user.setEmail(tab[i]);
            //user.updateProfilePicture();
        }

        //set picture wishlist


        //set wish picture wish
        Wish toSet = new Wish(0, null, null, 0, null);
        for(int id = 1; id < 16; id++){
            toSet.setId(id);
            //toSet.setPicture();
        }

        //link product and wishlist
        req = "INSERT INTO Content (wishlist, product) VALUES ";
        db.execSQL(req + "(18, 7);");
        db.execSQL(req + "(1, 1);");
        db.execSQL(req + "(2, 2);");
        db.execSQL(req + "(2, 3);");
        db.execSQL(req + "(3, 8);");
        db.execSQL(req + "(6, 2);");
        db.execSQL(req + "(10, 12);");
        db.execSQL(req + "(12, 15);");
        db.execSQL(req + "(13, 4);");
        db.execSQL(req + "(13, 14);");
        db.execSQL(req + "(14, 13);");
        db.execSQL(req + "(15, 10);");
        db.execSQL(req + "(15, 11);");
        db.execSQL(req + "(17, 1);");
        db.execSQL(req + "(17, 5);");
        db.execSQL(req + "(17, 6);");
        db.execSQL(req + "(20, 9);");

        //friend relation
        req = "INSERT INTO Friend (mail_host, relation, mail_requested) VALUES ";
        db.execSQL(req + "(\"" +tab[0] + "\", 1, \"" + tab[1] + "\");");
        db.execSQL(req + "(\"" +tab[0] + "\", 1, \"" + tab[2] + "\");");
        db.execSQL(req + "(\"" +tab[0] + "\", 0, \"" + tab[3] + "\");");
        db.execSQL(req + "(\"" +tab[0] + "\", 0, \"" + tab[4] + "\");");
        db.execSQL(req + "(\"" +tab[1] + "\", 0, \"" + tab[2] + "\");");
        db.execSQL(req + "(\"" +tab[2] + "\", 1, \"" + tab[7] + "\");");
        db.execSQL(req + "(\"" +tab[2] + "\", 0, \"" + tab[4] + "\");");
        db.execSQL(req + "(\"" +tab[2] + "\", 0, \"" + tab[5] + "\");");
        db.execSQL(req + "(\"" +tab[3] + "\", 0, \"" + tab[2] + "\");");
        db.execSQL(req + "(\"" +tab[3] + "\", 1, \"" + tab[1] + "\");");
        db.execSQL(req + "(\"" +tab[4] + "\", 1, \"" + tab[7] + "\");");
        db.execSQL(req + "(\"" +tab[4] + "\", 0, \"" + tab[5] + "\");");
        db.execSQL(req + "(\"" +tab[4] + "\", 1, \"" + tab[6] + "\");");
        db.execSQL(req + "(\"" +tab[5] + "\", 0, \"" + tab[7] + "\");");
        db.execSQL(req + "(\"" +tab[5] + "\", 1, \"" + tab[6] + "\");");
        db.execSQL(req + "(\"" +tab[6] + "\", 0, \"" + tab[1] + "\");");
        db.execSQL(req + "(\"" +tab[6] + "\", 1, \"" + tab[2] + "\");");
        db.execSQL(req + "(\"" +tab[6] + "\", 0, \"" + tab[0] + "\");");
        db.execSQL(req + "(\"" +tab[7] + "\", 1, \"" + tab[0] + "\");");
        db.execSQL(req + "(\"" +tab[7] + "\", 0, \"" + tab[1] + "\");");

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
