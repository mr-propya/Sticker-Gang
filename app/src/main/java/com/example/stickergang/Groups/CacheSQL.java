package com.example.stickergang.Groups;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.stickergang.Helpers.Constants;
import com.example.stickergang.WAHelpers.Sticker;
import com.example.stickergang.WAHelpers.StickerGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.stickergang.Helpers.Constants.SqlHelper.*;

public class CacheSQL extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sticker_details.db";
    Context c;

    public CacheSQL(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.c=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDB = "CREATE TABLE "+ TABLE_MASTER_DETAILS + " ( id integer PRIMARY KEY AUTOINCREMENT , "+ STICKER_PACK_IDENTIFIER +" TEXT UNIQUE , " +
                STICKER_PACK_NAME + " TEXT , "+
                STICKER_PACK_TRAY + " TEXT , "+
                STICKER_ENABLED +" int DEFAULT 1 )";

        String createStickersDB = "CREATE TABLE "+TABLE_STICKER_DETAILS + " ( id integer PRIMARY KEY AUTOINCREMENT , "+
                STICKER_INDIVIDUAL_DISPLAY+" TEXT , "+
                STICKER_INDIVIDUAL_IDENTIFIER+" TEXT UNIQUE , "+
                STICKER_PACK_IDENTIFIER+" TEXT , "+
                STICKER_ENABLED+" int DEFAULT 1 )";



        db.execSQL(createDB);
        db.execSQL(createStickersDB);
        fetchDetails(db,isSuccessful -> {
            Log.i("FetchInitial",""+isSuccessful);
        });
    }

    private void updateGroup(final SQLiteDatabase db,String groupId,final SqlCompletionCallBack callBack){

        String ref = String.format("groups/%s",groupId);
        FirebaseDatabase.getInstance().getReference(ref).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for(DataSnapshot d : dataSnapshot.child("stickers").getChildren()){
                    addNewSticker(db,groupId,d.getKey(),d.child("name").getValue(String.class));
                    downloadFile(groupId,d.getKey(),d.child("url").getValue(String.class),"webp");
                }

                String tray = dataSnapshot.child("tray").getValue(String.class);
                String name = dataSnapshot.child("name").getValue(String.class);
                addNewPack(db,groupId,name);
                downloadFile(groupId,"trayIcon",tray,"png");

                if(callBack!=null)
                    callBack.didQueryComplete(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(callBack!=null)
                    callBack.didQueryComplete(false);
            }
        });




    }

    private void downloadFile(String groupId, String key, String url, String format) {
        String subPath = Constants.ImageConstants.EXERNAL_FILE+"/"+String.format("%s/%s.%s",groupId,key,format);
        File f = new File(c.getExternalFilesDir(null),subPath);
        if(f.exists())
            return;

        DownloadManager downloadManager = (DownloadManager)c.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDestinationInExternalFilesDir(c,null, subPath);
        request.setVisibleInDownloadsUi(false);
        downloadManager.enqueue(request);


    }

    public void syncDB(SqlCompletionCallBack callBack){
//        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        this.fetchDetails(null,callBack);
//        writableDatabase.close();
    }

    private void fetchDetails(final SQLiteDatabase db,SqlCompletionCallBack callBack) {


        String ref = String.format("users/%s/groups","uidSelf");

        FirebaseDatabase.getInstance().getReference(ref).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AtomicInteger count = new AtomicInteger((int) dataSnapshot.getChildrenCount());
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    updateGroup(db,d.getKey(), isSuccessful ->{
                        if(!isSuccessful){
                            if(callBack!=null)
                                callBack.didQueryComplete(false);
                        }else{
                            count.getAndDecrement();
                            if(count.get()==0){
                                if(callBack!=null)
                                    callBack.didQueryComplete(true);
                            }

                        }
                    });
                    deleteExcessGroups(dataSnapshot);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(callBack!=null)
                    callBack.didQueryComplete(false);
            }
        });
    }

    private void deleteExcessGroups(DataSnapshot dataSnapshot) {
        //TODO
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public ArrayList<StickerGroup> getAllPacks(boolean restrict){

        ArrayList<StickerGroup> stickerGroups = new ArrayList<>();

        String selection = STICKER_ENABLED + " = ?";
        String[] selectionArgs = { "1" };

        if(!restrict){
            selection = null;
            selectionArgs = null;
        }



        Cursor query = this.getReadableDatabase().query(TABLE_MASTER_DETAILS, null, selection, selectionArgs, null, null, null);

        while (query.moveToNext())
            stickerGroups.add(new StickerGroup(query));

        query.close();

        return stickerGroups;

    }

    public ArrayList<Sticker> getAllStickers(String groupId){
        Log.i("Getting all stickers",groupId);
        ArrayList<Sticker> stickers = new ArrayList<>();

        String selection = STICKER_PACK_IDENTIFIER + " = ?";
        String[] selectionArgs = { groupId };

        Cursor query = this.getReadableDatabase().query(TABLE_STICKER_DETAILS, null, selection, selectionArgs, null, null, null);

        while (query.moveToNext())
            stickers.add(new Sticker(query,groupId));

        query.close();

        return stickers;
    }

    private void addNewPack(SQLiteDatabase db, String identifier, String packName){
        addNewPack(db,identifier,packName,identifier+".png");
    }

    private void addNewPack(SQLiteDatabase db, String identifier, String packName, String tray){

        boolean shouldClose = false;

        if(db==null){
            db = this.getWritableDatabase();
            shouldClose = true;
        }

        ContentValues cv = new ContentValues();
        cv.put(STICKER_PACK_IDENTIFIER,identifier);
        cv.put(STICKER_PACK_TRAY,tray);
        cv.put(STICKER_PACK_NAME,packName);
        cv.put(STICKER_ENABLED,1);
        db.insert(TABLE_MASTER_DETAILS,null,cv);

        if(shouldClose)
            db.close();

    }

    private void addNewSticker(SQLiteDatabase db,String groupIdentifier, String fileName, String display){

        boolean shouldClose = false;

        if(db==null){
            db = this.getWritableDatabase();
            shouldClose = true;
        }

        if(!fileName.endsWith(".webp"))
            fileName+=".webp";

        ContentValues cv = new ContentValues();
        cv.put(STICKER_INDIVIDUAL_IDENTIFIER,String.format("%s_%s",groupIdentifier,fileName));
        cv.put(STICKER_INDIVIDUAL_DISPLAY,display);
        cv.put(STICKER_PACK_IDENTIFIER,groupIdentifier);

        db.insert(TABLE_STICKER_DETAILS,null,cv);

        if(shouldClose)
            db.close();

    }

    public interface SqlCompletionCallBack{
        public void didQueryComplete(boolean isSuccessful);
    }

}
