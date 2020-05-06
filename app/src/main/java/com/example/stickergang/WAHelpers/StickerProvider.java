package com.example.stickergang.WAHelpers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.stickergang.BuildConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.stickergang.Helpers.Constants.WAP.*;



public class StickerProvider extends ContentProvider {

    UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final String METADATA = "metadata";
    private static final int METADATA_CODE = 1;

    private static final int METADATA_CODE_FOR_SINGLE_PACK = 2;

    static final String STICKERS = "stickers";
    private static final int STICKERS_CODE = 3;

    static final String STICKERS_ASSET = "stickers_asset";
    private static final int STICKERS_ASSET_CODE = 4;

    private static final int STICKER_PACK_TRAY_ICON_CODE = 5;

    ArrayList<String> allStickerList;

    @Override
    public boolean onCreate() {
        Log.i("Content provider","Oncreate called");
        final String authority = BuildConfig.APPLICATION_ID;
        //the call to get the metadata for the sticker packs.
        MATCHER.addURI(authority, METADATA, METADATA_CODE);

        //the call to get the metadata for single sticker pack. * represent the identifier
        MATCHER.addURI(authority, METADATA + "/*", METADATA_CODE_FOR_SINGLE_PACK);

        //gets the list of stickers for a sticker pack, * respresent the identifier.
        MATCHER.addURI(authority, STICKERS + "/*", STICKERS_CODE);

        ImageAssistants imageAssistants = new ImageAssistants(getContext());
                    MATCHER.addURI(authority, STICKERS_ASSET + "/" + "abc_identifier" + "/" + "trayImage", STICKER_PACK_TRAY_ICON_CODE);
        if(allStickerList == null)
            allStickerList = imageAssistants.getImageList(null);
        for(String s:allStickerList){
            MATCHER.addURI(authority,STICKERS_ASSET + "/" + "abc_identifier" + "/" + s, STICKERS_ASSET_CODE);
        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Log.i("Incoming query",uri.toString());
        List<String> pathSegments = uri.getPathSegments();
        int length = pathSegments.size();

        switch (length){
            case 1:
                if(pathSegments.get(0).equals(METADATA))
                    return getMasterDetails(uri);
            case 2:
                if(pathSegments.get(0).equals(METADATA))
                    return getCursorForSingleStickerPack(uri,pathSegments.get(1));
                if(pathSegments.get(0).equals(STICKERS))
                    return getStickersForAStickerPack(uri);
            case 4:
                if(pathSegments.get(0).equals(STICKERS_ASSET))
                    return getCursorForSingleStickerPack(uri,pathSegments.get(1));





        }



        final int code = MATCHER.match(uri);
        if (code == METADATA_CODE) {
            return getMasterDetails(uri);
        } else if (code == METADATA_CODE_FOR_SINGLE_PACK) {
            return getCursorForSingleStickerPack(uri,null);
        } else if (code == STICKERS_CODE) {
            return getStickersForAStickerPack(uri);
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    private Cursor getMasterDetails(Uri uri) {
        MatrixCursor cursor = new MatrixCursor(
                new String[]{
                        STICKER_PACK_IDENTIFIER_IN_QUERY,
                        STICKER_PACK_NAME_IN_QUERY,
                        STICKER_PACK_PUBLISHER_IN_QUERY,
                        STICKER_PACK_ICON_IN_QUERY,
                        ANDROID_APP_DOWNLOAD_LINK_IN_QUERY,
                        IOS_APP_DOWNLOAD_LINK_IN_QUERY,
                        PUBLISHER_EMAIL,
                        PUBLISHER_WEBSITE,
                        PRIVACY_POLICY_WEBSITE,
                        LICENSE_AGREENMENT_WEBSITE,
                        IMAGE_DATA_VERSION,
                        AVOID_CACHE,
                });

            MatrixCursor.RowBuilder builder = cursor.newRow();
            builder.add("abc_identifier");
            builder.add("Trial");
            builder.add("stickerPack.publisher");
            builder.add("base.png");
            builder.add("");
            builder.add("");
            builder.add("");
            builder.add("");
            builder.add("");
            builder.add("");
            builder.add(1);
            builder.add(0);

        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }

    private Cursor getStickersForAStickerPack(Uri uri) {
        final String identifier = uri.getLastPathSegment();
        MatrixCursor cursor = new MatrixCursor(new String[]{STICKER_FILE_NAME_IN_QUERY, STICKER_FILE_EMOJI_IN_QUERY});
        for(String s:allStickerList)
            cursor.addRow(new Object[]{s, null});

        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }

    private Cursor getCursorForSingleStickerPack(Uri uri,String stickerPack) {
        return getMasterDetails(uri);
    }


    @Override
    public String getType(@NonNull Uri uri) {
        final int matchCode = MATCHER.match(uri);
        switch (matchCode) {
            case METADATA_CODE:
                return "vnd.android.cursor.dir/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + METADATA;
            case METADATA_CODE_FOR_SINGLE_PACK:
                return "vnd.android.cursor.item/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + METADATA;
            case STICKERS_CODE:
                return "vnd.android.cursor.dir/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + STICKERS;
            case STICKERS_ASSET_CODE:
                return "image/webp";
            case STICKER_PACK_TRAY_ICON_CODE:
                return "image/png";
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public AssetFileDescriptor openAssetFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {
        final int matchCode = MATCHER.match(uri);
        Log.i("Incoming image",uri.toString());

            ImageAssistants imageAssistants = new ImageAssistants(getContext());
            List<String> pathSegments = uri.getPathSegments();
            for(String s:pathSegments)
                Log.i("incoming URI",s);
            return new AssetFileDescriptor(ParcelFileDescriptor.open(imageAssistants.getFile(pathSegments.get(1),pathSegments.get(2)), ParcelFileDescriptor.MODE_READ_ONLY), 0, AssetFileDescriptor.UNKNOWN_LENGTH);

    }




}
