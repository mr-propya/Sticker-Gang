package com.example.stickergang.WAHelpers;

import android.database.Cursor;

import com.example.stickergang.Helpers.Constants;

public class StickerGroup {

    private String identifier,name,tray;
    private boolean isAvail;

    public StickerGroup(Cursor query){
        this.identifier = query.getString(query.getColumnIndex(Constants.SqlHelper.STICKER_PACK_IDENTIFIER));
        this.name = query.getString(query.getColumnIndex(Constants.SqlHelper.STICKER_PACK_NAME));
        this.tray = query.getString(query.getColumnIndex(Constants.SqlHelper.STICKER_PACK_TRAY));
        this.isAvail =query.getInt(query.getColumnIndex(Constants.SqlHelper.STICKER_ENABLED))==1;
    }


    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getTray() {
        return tray;
    }

    public boolean isAvail() {
        return isAvail;
    }
}
