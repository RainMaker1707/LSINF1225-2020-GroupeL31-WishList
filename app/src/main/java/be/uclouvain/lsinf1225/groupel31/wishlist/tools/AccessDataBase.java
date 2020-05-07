package be.uclouvain.lsinf1225.groupel31.wishlist.tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AccessDataBase {
    private MySQLiteOpenHelper SQLiteHelper;
    private SQLiteDatabase db;
    private Context context;

    public AccessDataBase(Context context){
        this.context = context;
        SQLiteHelper = new MySQLiteOpenHelper(context, "wishlist_db.db", null, 1);
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

    public SQLiteDatabase get(){return this.db;}
}
