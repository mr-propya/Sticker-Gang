package com.example.stickergang.WAHelpers;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stickergang.Helpers.ActivityHelper;


public class AddStickerHelpers {
    Context c;
    ActivityHelper activity;

    public AddStickerHelpers(Context c) {
        this.c = c;
        this.activity = ((ActivityHelper)c);
    }

    boolean isStickerPackRegistered(){

        return false;
    }

    boolean isNormal(){
        return true;
    }

    public void registerStickerPack(String identifier,String authority,String stickerPackName){
        Intent intent = new Intent();
        intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
        intent.putExtra("sticker_pack_id", identifier); //identifier is the pack's identifier in contents.json file
        intent.putExtra("sticker_pack_authority", authority); //authority is the ContentProvider's authority. In the case of the sample app it is BuildConfig.CONTENT_PROVIDER_AUTHORITY.
        intent.putExtra("sticker_pack_name", stickerPackName); //stickerPackName is the name of the sticker pack.
        try {
            activity.startActivityForResult(intent, new ActivityHelper.ActivityResultResponse() {
                @Override
                public void result(Intent data, int result) {
                    activity.showToast(""+result);
                    if(data.hasCategory("validation_error"))
                        Log.i("Adding sticker",data.getStringExtra("validation_error"));
                    Log.i("Adding sticker",""+result);
                }
            });
        } catch (ActivityNotFoundException e) {
            ((ActivityHelper)c).showToast(e.getLocalizedMessage());
        }
    }



}
