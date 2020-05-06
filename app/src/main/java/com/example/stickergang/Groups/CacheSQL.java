package com.example.stickergang.Groups;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.stickergang.WAHelpers.StickerGroup;

import java.util.ArrayList;

import static com.example.stickergang.Helpers.Constants.SqlHelper.*;

public class CacheSQL extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sticker_details.db";

    public CacheSQL(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDB = "CREATE TABLE "+ MASTER_DETAILS + " ( id integer PRIMARY KEY AUTOINCREMENT , "+ STICKER_PACK_IDENTIFIER +" TEXT UNIQUE , " +
                STICKER_PACK_NAME + " TEXT , "+
                STICKER_PACK_TRAY + " TEXT , "+
                STICKER_PACK_ENABLED+" int DEFAULT 1 )";
        db.execSQL(createDB);
        fetchDetails();
    }

    private void fetchDetails() {
        for(int i=0;i<10;i++)
            addNewPack("identifier"+i,"pack"+i);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public ArrayList<StickerGroup> getAllPacks(boolean restrict){

        ArrayList<StickerGroup> stickerGroups = new ArrayList<>();

        String selection = STICKER_PACK_ENABLED + " = ?";
        String[] selectionArgs = { "1" };

        if(!restrict){
            selection = null;
            selectionArgs = null;
        }

        Cursor query = this.getReadableDatabase().query(MASTER_DETAILS, null, selection, selectionArgs, null, null, null);

        while (query.moveToNext())
            stickerGroups.add(new StickerGroup(query));

        query.close();

        return stickerGroups;

    }

    private void addNewPack(String identifier, String packName){
        ContentValues cv = new ContentValues();
        cv.put(STICKER_PACK_IDENTIFIER,identifier);
        cv.put(STICKER_PACK_TRAY,identifier+".png");
        cv.put(STICKER_PACK_NAME,packName);
        cv.put(STICKER_PACK_ENABLED,1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                CacheSQL.this.getWritableDatabase().insert(MASTER_DETAILS,null,cv);
            }
        }).start();
    }

}
