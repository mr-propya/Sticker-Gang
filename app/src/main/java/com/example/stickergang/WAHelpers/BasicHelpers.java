package com.example.stickergang.WAHelpers;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;

import static com.example.stickergang.Helpers.Constants.WAP.*;


public class BasicHelpers {
    Context c;


    public BasicHelpers(Context c) throws Exception {
        this.c = c;
        if(!(isPackageInstalled(CONSUMER_WHATSAPP_PACKAGE_NAME,c.getPackageManager()) ||
                isPackageInstalled(SMB_WHATSAPP_PACKAGE_NAME,c.getPackageManager()))){
            throw new Exception("Whatsapp not found");
        }


    }



    private  boolean isWhitelistedFromProvider(@NonNull Context context, @NonNull String identifier, String whatsappPackageName) {
        final PackageManager packageManager = context.getPackageManager();
        if (isPackageInstalled(whatsappPackageName, packageManager)) {
            final String whatsappProviderAuthority = whatsappPackageName + CONTENT_PROVIDER;
            final ProviderInfo providerInfo = packageManager.resolveContentProvider(whatsappProviderAuthority, PackageManager.GET_META_DATA);
            // provider is not there. The WhatsApp app may be an old version.
            if (providerInfo == null) {
                return false;
            }
            final Uri queryUri = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(whatsappProviderAuthority).appendPath(QUERY_PATH).appendQueryParameter(AUTHORITY_QUERY_PARAM, STICKER_APP_AUTHORITY).appendQueryParameter(IDENTIFIER_QUERY_PARAM, identifier).build();
            try (final Cursor cursor = context.getContentResolver().query(queryUri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    final int whiteListResult = cursor.getInt(cursor.getColumnIndexOrThrow(QUERY_RESULT_COLUMN_NAME));
                    return whiteListResult == 1;
                }
            }
        } else {
            //if app is not installed, then don't need to take into its whitelist info into account.
            return true;
        }
        return false;
    }



    boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
                return applicationInfo.enabled;
            } else {
                return false;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}



