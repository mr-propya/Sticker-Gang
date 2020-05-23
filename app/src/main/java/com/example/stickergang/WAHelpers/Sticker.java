package com.example.stickergang.WAHelpers;

import android.content.Context;
import android.database.Cursor;

import com.example.stickergang.Helpers.Constants;

import java.io.File;

public class Sticker {
    String identifier,name,groupId;
    boolean hasCached = false;
    File file;

    public Sticker(Cursor cursor,String groupId){
        this.identifier = cursor.getString(cursor.getColumnIndex(Constants.SqlHelper.STICKER_INDIVIDUAL_IDENTIFIER));
        this.name = cursor.getString(cursor.getColumnIndex(Constants.SqlHelper.STICKER_INDIVIDUAL_DISPLAY));
        this.groupId = groupId;
    }

    public File getFile(Context c){
        if(!hasCached){
            ImageAssistants helper = new ImageAssistants(c);
            helper.getFile(groupId,identifier+".webp");
            hasCached = true;
        }
        return file;
    }


    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getGroupId() {
        return groupId;
    }

    public boolean isHasCached() {
        return hasCached;
    }

}
