package be.uclouvain.lsinf1225.groupel31.wishlist.tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class AccessDataBase {
    private String dbName = "wishlist_db.db";
    private Integer version = 1;
    private MySQLiteOpenHelper SQLiteHelper;
    private SQLiteDatabase db;
    private Context context;

    public AccessDataBase(Context context){
        this.context = context;
        SQLiteHelper = new MySQLiteOpenHelper(context, dbName, null, version);
    }

    public void insert(String req){
        db = SQLiteHelper.getWritableDatabase();
        db.execSQL(req);
    }

    public Cursor select(String req){
        db = SQLiteHelper.getReadableDatabase();
        return db.rawQuery(req,null);
    }

    public Context getContext() {
        return context;
    }
}
